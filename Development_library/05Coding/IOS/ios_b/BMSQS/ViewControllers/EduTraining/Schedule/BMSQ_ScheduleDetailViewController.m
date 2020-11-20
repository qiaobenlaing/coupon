//
//  BMSQ_ScheduleDetailViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/8.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_ScheduleDetailViewController.h"
#import "CourseIntroductionViewCell.h"
#import "OpenClassUtil.h"
#import "SVProgressHUD.h"
#import "ContactViewCell.h"
#import "CourseCommentViewCell.h"
#import "UIImageView+WebCache.h"
#import <ShareSDK/ShareSDK.h>
#import "HQShareUtils.h"
#import "OpenClassCell.h"

#define OPENCLASSCELLHEIGHT 40.0
//课程时间   cell
@interface BMSQ_ScheduleDetailSyllabusCell : UITableViewCell

@property (nonatomic, strong)UILabel *leftLabel;
@property (nonatomic, strong)UIView *backView;
@property (nonatomic, strong)UIView *lineView;

@end

@implementation BMSQ_ScheduleDetailSyllabusCell

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
    
    
    self.lineView.frame = CGRectMake(0, OPENCLASSCELLHEIGHT * k, APP_VIEW_WIDTH, 1);
    
}

@end

@interface BMSQ_ScheduleDetailViewController ()<UITableViewDataSource, UITableViewDelegate>
{
    int page;
}
@property (nonatomic, strong)UITableView * tableView;
@property (nonatomic, strong)NSDictionary * dataSource;
@property (nonatomic, strong)UIImageView * iconImage;
@property (nonatomic, strong) NSMutableArray * dataSorceAry;

@end

@implementation BMSQ_ScheduleDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    [self setNavTitle:[NSString stringWithFormat:@"%@--课程详情", self.className]];
    [self setNavBackItem];
    page = 1;
    [self setViewUp];
    [self getShopClassInfo];
    
    
}


- (void)setViewUp
{
    self.iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/3)];
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 64) style:UITableViewStyleGrouped];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.tableHeaderView = self.iconImage;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:self.tableView];
}

#pragma mark -------- UITableViewDataSource, UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    return 9;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 2) {
        static NSString *identifier = @"OpenClassSyllabusCell";
        BMSQ_ScheduleDetailSyllabusCell *cell = (BMSQ_ScheduleDetailSyllabusCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[BMSQ_ScheduleDetailSyllabusCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        
        [cell setCellValue:[self.dataSource objectForKey:@"classWeekInfo"]];
        
        return cell;
        
    } else if (indexPath.row == 8) { //课程简介
        static NSString *identifier = @"openClassInfo";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            
            [OpenClassUtil openClassSetLabel:CGRectMake(10, 0, 100, 40.0) text:@"课程简介" font:[UIFont systemFontOfSize:13.f] textColor:[UIColor blackColor] view:cell.contentView];
            
            UILabel *infoLabel = [OpenClassUtil openClassSetLabel:CGRectMake(110, 0, APP_VIEW_WIDTH-110, 40.0) text:@"XXXX" font:[UIFont systemFontOfSize:13.f] textColor:[UIColor blackColor] view:cell.contentView];
            infoLabel.tag = 2008;
            infoLabel.numberOfLines = 0;
            [cell.contentView addSubview:infoLabel];
            
            UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 40.0-0.5, APP_VIEW_WIDTH, 0.5)];
            lineView.backgroundColor = APP_CELL_LINE_COLOR;
            lineView.tag = 3000;
            [cell.contentView addSubview:lineView];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
            UILabel *infolabel = [cell.contentView viewWithTag:2008];
            UIView *lineView = [cell.contentView viewWithTag:3000];
            
            infolabel.text= [NSString stringWithFormat:@"%@", [self.dataSource objectForKey:@"classInfo"]];
            
            CGSize infoSize = [OpenClassUtil getInfolabelSize:infolabel.text];
            if (infoSize.height>30) {
                infolabel.frame = CGRectMake(110, 40.0/2-13.0/2, infoSize.width, infoSize.height);
                lineView.frame = CGRectMake(0, infolabel.frame.origin.y+infoSize.height-0.5, APP_VIEW_WIDTH, 0.5);
            }else {
                infolabel.frame = CGRectMake(110, 0, APP_VIEW_WIDTH-110, 40.0);
                lineView.frame= CGRectMake(0, 40.0-0.5, APP_VIEW_WIDTH, 0.5);
            }
        
        
        return cell;
        
    }else{
        static NSString *identifier = @"OpenClassCelldes";
        OpenClassCell *cell = (OpenClassCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[OpenClassCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
        }
        
        [cell setCellValue:self.dataSource forRow:(int)indexPath.row];
        cell.buttonEx.hidden = YES;
        return cell;
    }
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1.0;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 30;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
        UIView * secondView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 30)];
        secondView.backgroundColor = [UIColor grayColor];
        
        UILabel * subject = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, 150, 20)];
        subject.text = [NSString stringWithFormat:@"课程介绍"];
        subject.font = [UIFont systemFontOfSize:14.0];
        [secondView addSubview:subject];
        
        return secondView;
        
       
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == 2) {
        NSArray *classWeekInfoArray = [self.dataSource objectForKey:@"classWeekInfo"];
        CGFloat count = 0;
        for (NSDictionary *learnTimeDic in classWeekInfoArray) {
            NSArray *array = [learnTimeDic objectForKey:@"learnTime"];
            
            count = count + array.count;
            
        }
        return  count*40.0 ;
    } else if (indexPath.row == 8) {
        NSString *infoStr = [NSString stringWithFormat:@"%@", [self.dataSource objectForKey:@"classInfo"]];
        CGSize size = [OpenClassUtil getInfolabelSize:infoStr];
        if (size.height>30) {
            return (40.0/2-13.0/2)+size.height+20;
        }else {
            return 40.0;
        }
        
        
    }
    
    return 40.0;
    
}


#pragma mark ----------  getShopClassInfo  获取某一个开班的详情
- (void)getShopClassInfo
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:self.classCode forKey:@"classCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopClassInfo" strParams:self.classCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopClassInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@ "%@", responseObject);
        self.dataSource = [NSDictionary dictionaryWithDictionary:responseObject];
        
        
        [self.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, [self.dataSource objectForKey:@"classUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
        
        [self.tableView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}




#pragma mark ----- 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
