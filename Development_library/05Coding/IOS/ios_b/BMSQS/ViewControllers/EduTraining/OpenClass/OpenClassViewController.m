//
//  OpenClassViewController.m
//  BMSQS
//
//  Created by gh on 16/3/3.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenClassViewController.h"
#import "MJRefresh.h"
#import "SVProgressHUD.h"
#import "OpenNewClassViewController.h"
#import "OpenClassUtil.h"
#import "OpenClassCell.h"
#import "UIImageView+WebCache.h"

#import "UIButtonEx.h"


#define OPENCLASSCELLHEIGHT 40.0

//课程时间   cell
@interface OpenClassSyllabusCell : UITableViewCell

@property (nonatomic, strong)UILabel *leftLabel;
@property (nonatomic, strong)UIView *backView;
@property (nonatomic, strong)UIView *lineView;

@end

@implementation OpenClassSyllabusCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setViewUP];
    }
    
    return self;
}
- (void)setViewUP {
    self.leftLabel = [OpenClassUtil openClassSetLabel:CGRectMake(10, 0, 100, OPENCLASSCELLHEIGHT) text:@"上课时间" font:[UIFont systemFontOfSize:13.f] textColor:[UIColor blackColor] view:self.contentView];

    self.lineView = [[UIView alloc] initWithFrame:CGRectMake(0, OPENCLASSCELLHEIGHT-0.5, APP_VIEW_WIDTH, 0.5)];
    self.lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [self.contentView addSubview:self.lineView];
    
}

- (void)setCellValue:(NSArray *)array {
    if (self.backView != nil) {
        [self.backView removeFromSuperview];
    }
    
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(110, 0, APP_VIEW_WIDTH-110, OPENCLASSCELLHEIGHT*array.count + 1)];
    [self.contentView addSubview:self.backView];
    
    int k = 0;
    for (int i=0; i<array.count; i++) {
        NSDictionary *dic = [array objectAtIndex:i];
        /*
        classWeekInfo	上课时间	array<object>
          learnTime	学习时间	array<object>
              EndTime	上课结束时间	string	格式：HH:MM
              startTime	上课开始时间	string	格式：HH:MM
          weekName	周几
         */
        NSString *weekName = [NSString stringWithFormat:@"%@",[dic objectForKey:@"weekName"]];
        [OpenClassUtil openClassSetLabel:CGRectMake(0, OPENCLASSCELLHEIGHT*k, 50, OPENCLASSCELLHEIGHT) text:weekName font:[UIFont systemFontOfSize:13.f] textColor:[UIColor blackColor] view:self.backView];
        
        NSArray *learnTimeArray = [dic objectForKey:@"learnTime"];
        for (NSDictionary *learTimeDic in learnTimeArray) {
            NSString *timeStr = [NSString stringWithFormat:@" %@-%@", [learTimeDic objectForKey:@"startTime"], [learTimeDic objectForKey:@"endTime"] ];
            [OpenClassUtil openClassSetLabel:CGRectMake(60, OPENCLASSCELLHEIGHT*k, APP_VIEW_WIDTH-110, OPENCLASSCELLHEIGHT) text:timeStr font:[UIFont systemFontOfSize:13.f] textColor:[UIColor blackColor] view:self.backView];
            
            
            k++;
        }
        
    }
    
    
    self.lineView.frame = CGRectMake(0, OPENCLASSCELLHEIGHT * k-0.5, APP_VIEW_WIDTH, 0.5);
    
}

@end





@interface OpenClassViewController () <UITableViewDataSource, UITableViewDelegate, openClassDelegate>
{
    int page;
    
}


@property (nonatomic, strong)NSMutableArray *classArray;
@property (nonatomic, strong)UITableView *tableView;

@end

@implementation OpenClassViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
}

- (void)setViewUp {
    
    
    NSNotificationCenter * center = [NSNotificationCenter defaultCenter];
    //添加当前类对象为一个观察者，name和object设置为nil，表示接收一切通知
    [center addObserver:self selector:@selector(OpenNewClassViewControllerIsFinish) name:@"OpenNewClassViewControllerIsFinish" object:nil];
    
    
    
    [self setNavBackItem];
    [self setNavTitle:@"开班"];
    [self setRightBtn];
    
    self.classArray = [[NSMutableArray alloc] init];
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.tableView.dataSource = self;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.delegate = self;
    [self.view addSubview:self.tableView];
    
    [self OpenNewClassViewControllerIsFinish];
    
}


//增加开班
- (void)btnAction:(UIButton *)button {
    NSLog(@"---->增加");
    
    OpenNewClassViewController *vc = [[OpenNewClassViewController alloc] init];
    [self.navigationController pushViewController:vc animated:YES];
    
    
}


#pragma mark - footerrereshing
- (void)footerRereshing {
    page ++;
    [self getShopClassList];

}

#pragma mark - UITableView delegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return self.classArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == 2) {
        NSDictionary *dic = [self.classArray objectAtIndex:indexPath.section];
        NSArray *classWeekInfoArray = [dic objectForKey:@"classWeekInfo"];
        CGFloat count = 0;
        for (NSDictionary *learnTimeDic in classWeekInfoArray) {
            NSArray *array = [learnTimeDic objectForKey:@"learnTime"];
            
            count = count + array.count;
            
        }
        return  count*OPENCLASSCELLHEIGHT ;
    } else if (indexPath.row == 9) {
        return 100;
        
    } else if (indexPath.row == 8) {
        NSDictionary *dic = [self.classArray objectAtIndex:indexPath.section];
        NSString *infoStr = [NSString stringWithFormat:@"%@", [dic objectForKey:@"classInfo"]];
        CGSize size = [OpenClassUtil getInfolabelSize:infoStr];
        if (size.height>30) {
            return (OPENCLASSCELLHEIGHT/2-13.0/2)+size.height+20;
        }else {
            return OPENCLASSCELLHEIGHT;
        }
        
        
    }
    
    return OPENCLASSCELLHEIGHT;
    
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 10;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
   
    
    if (indexPath.row == 2) {
        static NSString *identifier = @"OpenClassSyllabusCell";
        OpenClassSyllabusCell *cell = (OpenClassSyllabusCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[OpenClassSyllabusCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];

        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        NSDictionary *dic = self.classArray[indexPath.section];
        [cell setCellValue:[dic objectForKey:@"classWeekInfo"]];
        
        return cell;
        
    } else if (indexPath.row == 8) { //课程简介
        static NSString *identifier = @"openClassInfo";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            
            [OpenClassUtil openClassSetLabel:CGRectMake(10, 0, 100, OPENCLASSCELLHEIGHT) text:@"课程简介" font:[UIFont systemFontOfSize:13.f] textColor:[UIColor blackColor] view:cell.contentView];
            
            UILabel *infoLabel = [OpenClassUtil openClassSetLabel:CGRectMake(110, 0, APP_VIEW_WIDTH-110, OPENCLASSCELLHEIGHT) text:@"XXXX" font:[UIFont systemFontOfSize:13.f] textColor:[UIColor blackColor] view:cell.contentView];
            infoLabel.tag = 2008;
            infoLabel.numberOfLines = 0;
            [cell.contentView addSubview:infoLabel];
            
            UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, OPENCLASSCELLHEIGHT-0.5, APP_VIEW_WIDTH, 0.5)];
            lineView.backgroundColor = APP_CELL_LINE_COLOR;
            lineView.tag = 3000;
            [cell.contentView addSubview:lineView];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        if (self.classArray.count > 0) {
            NSDictionary *dic = [self.classArray objectAtIndex:indexPath.section];
            UILabel *infolabel = [cell.contentView viewWithTag:2008];
            UIView *lineView = [cell.contentView viewWithTag:3000];

            infolabel.text= [NSString stringWithFormat:@"%@", [dic objectForKey:@"classInfo"]];
            
            CGSize infoSize = [OpenClassUtil getInfolabelSize:infolabel.text];
            if (infoSize.height>30) {
                infolabel.frame = CGRectMake(110, OPENCLASSCELLHEIGHT/2-13.0/2, infoSize.width, infoSize.height);
                lineView.frame = CGRectMake(0, infolabel.frame.origin.y+infoSize.height-0.5, APP_VIEW_WIDTH, 0.5);
            }else {
                infolabel.frame = CGRectMake(110, 0, APP_VIEW_WIDTH-110, OPENCLASSCELLHEIGHT);
                lineView.frame= CGRectMake(0, OPENCLASSCELLHEIGHT-0.5, APP_VIEW_WIDTH, 0.5);
            }
            
        }
        
        return cell;
        
    } else if (indexPath.row == 9) {//展示图片
        static NSString *identifier = @"openClassImage";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 100)];
            imageView.contentMode = UIViewContentModeScaleAspectFit;
            imageView.backgroundColor = [UIColor clearColor];
            imageView.tag = 2009;
            [cell.contentView addSubview:imageView];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        UIImageView *imageView = [cell.contentView viewWithTag:2009];
        NSDictionary *dic = [self.classArray objectAtIndex:indexPath.section];
        NSString *imageUrl = [NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, [dic objectForKey:@"classUrl"]];
        [imageView sd_setImageWithURL:[NSURL URLWithString:imageUrl] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
        
        
        return cell;
        
    }
    
    else {
        static NSString *identifier = @"OpenClassCell";
        OpenClassCell *cell = (OpenClassCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[OpenClassCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
       
        
        cell.openCellDelegate = self;
        NSDictionary *dic = self.classArray[indexPath.section];
        [cell setCellValue:dic forRow:(int)indexPath.row];

//        if (indexPath.row == 0 & indexPath.section == 0) {
//            cell.buttonEx.hidden = NO;
//        }else {
//            cell.buttonEx.hidden = YES;
//        }
        
        
        return cell;
        
    }

    return nil;
    
    
}




- (void)getShopClassList {
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:page] forKey:@"page"];
    
    NSString* vcode = [gloabFunction getSign:@"getShopClassList" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];

    [self.jsonPrcClient invokeMethod:@"getShopClassList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [self.tableView footerEndRefreshing];
        
        if (page == 1) {
            [self.classArray removeAllObjects];
        }
        NSArray *array = [responseObject objectForKey:@"shopClassList"];
        
        [self.classArray addObjectsFromArray:array];

        if ([[responseObject objectForKey:@"totalCount"] intValue] ==  self.classArray.count) {
            [self.tableView removeFooter];
        }
        
        [self.tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        [self.tableView footerEndRefreshing];
    }];
    
    
}



#pragma mark - 删除开课
- (void)btnDelShopClass:(NSString *)classCode {
    
    
    NSLog(@"--->删除开课classCode%@",classCode);
    
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:classCode forKey:@"classCode"];
    [params setObject:[NSNumber numberWithInt:page] forKey:@"page"];
    
    NSString* vcode = [gloabFunction getSign:@"delShopClass" strParams:classCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"delShopClass" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSString *code = [responseObject objectForKey:@"code"];
        switch (code.intValue) {
            case 50000:
                [self OpenNewClassViewControllerIsFinish];
                
                break;
            case 20000:
                CSAlert(@"删除失败");
                break;
            case 77015:
                CSAlert(@"有人报名不能删除");
                break;
            default:{
                NSString *errorString = [NSString stringWithFormat:@"错误信息:%@", code];
                CSAlert(errorString);
            }
                break;
                
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(@"删除失败");
        
    }];
    
    
}

//初始开课列表   && 新增开课后  调用 刷新列表
- (void)OpenNewClassViewControllerIsFinish {
    [self.tableView setContentOffset:CGPointMake(0,0) animated:YES];
    page = 1;
    [self.tableView addFooterWithTarget:self action:@selector(footerRereshing)];
    [self getShopClassList];
}


- (void)setRightBtn {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH-54, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + APP_STATUSBAR_HEIGHT, 44, APP_NAV_LEFT_ITEM_HEIGHT);
    button.backgroundColor = [UIColor clearColor];
    [button setTitle:@"增加" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [button.titleLabel setTextAlignment:NSTextAlignmentRight];
    [self setRight:button];
    
    
}



@end
