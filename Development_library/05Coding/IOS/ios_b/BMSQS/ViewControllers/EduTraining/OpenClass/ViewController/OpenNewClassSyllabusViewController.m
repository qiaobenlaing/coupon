//
//  OpenNewClassSyllabusViewController.m
//  BMSQS
//
//  Created by gh on 16/3/16.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenNewClassSyllabusViewController.h"
#import "OpenClassUtil.h"
#import "ClassSyllabusCell.h"
#import "OpenClassTimePickerView.h"

@interface OpenNewClassSyllabusViewController () <UITableViewDataSource, UITableViewDelegate, OpenClassTimeDelegate>

@property (nonatomic, strong)UITableView *tableView1;
@property (nonatomic, strong)UITableView *tableView2;
@property (nonatomic, strong)UITableView *tableView3;
@property (nonatomic, strong)UITableView *tableView4;
@property (nonatomic, strong)UITableView *tableView5;
@property (nonatomic, strong)UITableView *tableView6;
@property (nonatomic, strong)UITableView *tableView7;

@property (nonatomic, strong)UIScrollView *scrollView;


@property (nonatomic, strong)NSMutableArray *array1;
@property (nonatomic, strong)NSMutableArray *array2;
@property (nonatomic, strong)NSMutableArray *array3;
@property (nonatomic, strong)NSMutableArray *array4;
@property (nonatomic, strong)NSMutableArray *array5;
@property (nonatomic, strong)NSMutableArray *array6;
@property (nonatomic, strong)NSMutableArray *array7;


@property (nonatomic, strong)OpenClassTimePickerView *timePickerView;


@end

@implementation OpenNewClassSyllabusViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
}

- (void)setViewUp {
    [self setNavBackItem];
    [self setNavTitle:@"课程表"];
    [self setRightBtn];
    //初始化数组
    [self loadArray];
    //初始化ScrollView
    [self setMyScrollView];
    //初始化tableView
    [self setAllTableView];
    
    
    self.timePickerView = [[OpenClassTimePickerView alloc] initWithFrame:CGRectMake(20, APP_VIEW_HEIGHT/3, APP_VIEW_WIDTH-40, 226)];
    self.timePickerView.delegate = self;
    [self.view addSubview:self.timePickerView];
    [self.timePickerView disMiss];
    
    
    
    
}





- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    if (section == 0) {
        if (tableView == self.tableView1) {
            return self.array1.count;
        }else if (tableView == self.tableView2) {
            return self.array2.count;
        }else if (tableView == self.tableView3) {
            return self.array3.count;
        }else if (tableView == self.tableView4) {
            return self.array4.count;
        }else if (tableView == self.tableView5) {
            return self.array5.count;
        }else if (tableView == self.tableView6) {
            return self.array6.count;
        }else if (tableView == self.tableView7) {
            return self.array7.count;
        }
        
    } else if (section == 1){
        return 1;
    }
    
    return 0;
}
//if (tableView == self.tableView1) {
//    
//}else if (tableView == self.tableView2) {
//    
//}else if (tableView == self.tableView3) {
//    
//}else if (tableView == self.tableView4) {
//    
//}else if (tableView == self.tableView5) {
//    
//}else if (tableView == self.tableView6) {
//    
//}else if (tableView == self.tableView7) {
//    
//}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0) {
        static NSString *identifier = @"ClassSyllabusCell";
        ClassSyllabusCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[ClassSyllabusCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        NSDictionary *timeDic;
        if (tableView == self.tableView1) {
            timeDic = [self.array1 objectAtIndex:indexPath.row];
        }else if (tableView == self.tableView2) {
            timeDic = [self.array2 objectAtIndex:indexPath.row];
        }else if (tableView == self.tableView3) {
            timeDic = [self.array3 objectAtIndex:indexPath.row];
        }else if (tableView == self.tableView4) {
            timeDic = [self.array4 objectAtIndex:indexPath.row];
        }else if (tableView == self.tableView5) {
            timeDic = [self.array5 objectAtIndex:indexPath.row];
        }else if (tableView == self.tableView6) {
            timeDic = [self.array6 objectAtIndex:indexPath.row];
        }else if (tableView == self.tableView7) {
            timeDic = [self.array7 objectAtIndex:indexPath.row];
        }
        [cell setCellValue:timeDic];
        
        
        return cell;
        
    }
    else if (indexPath.section == 1){
        static NSString *bottomIdentifier = @"bottomID";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:bottomIdentifier];
        if (!cell) {
            cell= [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:bottomIdentifier];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.textAlignment = NSTextAlignmentCenter;
        cell.textLabel.text = @"添加时间";
        return cell;
    }

    
    return nil;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    if (section == 0) {
        return 41;
        
    }
    return 0;
    
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    if (section == 0) {
        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/2, 41)];
        view.backgroundColor = [UIColor whiteColor];
        
        NSString *textStr = @"";
        if (tableView == self.tableView1) {
            textStr = @"周一";
        }else if (tableView == self.tableView2) {
            textStr = @"周二";
        }else if (tableView == self.tableView3) {
            textStr = @"周三";
        }else if (tableView == self.tableView4) {
            textStr = @"周四";
        }else if (tableView == self.tableView5) {
            textStr = @"周五";
        }else if (tableView == self.tableView6) {
            textStr = @"周六";
        }else if (tableView == self.tableView7) {
            textStr = @"周日";
        }
        
        UILabel *label = [OpenClassUtil openClassSetLabel:CGRectMake(0, 0, APP_VIEW_WIDTH/2, 40) text:textStr font:[UIFont systemFontOfSize:14.f] textColor:nil view:view];
        label.textAlignment = NSTextAlignmentCenter;
        
        [gloabFunction ggsetLineView:CGRectMake(0, 40, APP_VIEW_WIDTH/2, 1) view:view];
        
        return view;
        
    }
    
    return nil;
}
//- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section {
//    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/2, 40)];
//    view.backgroundColor = [UIColor whiteColor];
//    
//    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
//    
//    
//    [OpenClassUtil openClassSetLabel:view.frame text:@"添加时间" font:[UIFont systemFontOfSize:14.f] textColor:nil view:view];
//    
//    return view;
//}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 1) {
        int tableViewID = 0;
        if (tableView == self.tableView1) {
            tableViewID = 1;
        }else if (tableView == self.tableView2) {
            tableViewID = 2;
        }else if (tableView == self.tableView3) {
            tableViewID = 3;
        }else if (tableView == self.tableView4) {
            tableViewID = 4;
        }else if (tableView == self.tableView5) {
            tableViewID = 5;
        }else if (tableView == self.tableView6) {
            tableViewID = 6;
        }else if (tableView == self.tableView7) {
            tableViewID = 7;
        }
        self.timePickerView.hidden = NO;
        self.timePickerView.tableViewID = tableViewID;
        
    }
    


}

//得到时间
- (void)getSelectTime:(NSDictionary *)dic reloadArray:(int)tableViewID {
    switch (tableViewID) {
        case 1:
            [self.array1 addObject:dic];
            [self.tableView1 reloadData];
            break;
        case 2:
            [self.array2 addObject:dic];
            [self.tableView2 reloadData];
            break;
        case 3:
            [self.array3 addObject:dic];
            [self.tableView3 reloadData];
            break;
        case 4:
            [self.array4 addObject:dic];
            [self.tableView4 reloadData];
            break;
        case 5:
            [self.array5 addObject:dic];
            [self.tableView5 reloadData];
            break;
        case 6:
            [self.array6 addObject:dic];
            [self.tableView6 reloadData];
            break;
        case 7:
            [self.array7 addObject:dic];
            [self.tableView7 reloadData];
            break;
            
        default:
            break;
    }
    

    
}



- (void)loadArray {
    self.array1 = [[NSMutableArray alloc] init];
    self.array2 = [[NSMutableArray alloc] init];
    self.array3 = [[NSMutableArray alloc] init];
    self.array4 = [[NSMutableArray alloc] init];
    self.array5 = [[NSMutableArray alloc] init];
    self.array6 = [[NSMutableArray alloc] init];
    self.array7 = [[NSMutableArray alloc] init];
    if (self.dataArray.count>0) {
        
        for (NSDictionary *dic in self.dataArray) {
            
            NSString *weekString = [dic objectForKey:@"weekName"];
            switch (weekString.intValue) {
                case 1:
                    [self.array1 addObject:dic];
                    break;
                case 2:
                    [self.array2 addObject:dic];
                    break;
                case 3:
                    [self.array3 addObject:dic];
                    break;
                case 4:
                    [self.array4 addObject:dic];
                    break;
                case 5:
                    [self.array5 addObject:dic];
                    break;
                case 6:
                    [self.array6 addObject:dic];
                    break;
                case 7:
                    [self.array7 addObject:dic];
                    break;

                default:
                    break;
            }
            
            
        }

        
    }
    
}

- (void)setMyScrollView {
    self.scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.scrollView.contentSize = CGSizeMake(APP_VIEW_WIDTH/2*7, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
    self.scrollView.bounces = NO;
    [self.view addSubview:self.scrollView];
}


- (void)setAllTableView {
    CGFloat tableViewWidht = APP_VIEW_WIDTH/2;
    CGFloat tableViewHeight = APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y;
    CGFloat originX = 0;
    
    self.tableView1 = [OpenClassUtil openClassSyllabusTableView:CGRectMake(originX, 0, tableViewWidht, tableViewHeight) view:self.scrollView theDelegate:self];
    originX = originX + tableViewWidht;
    self.tableView2 = [OpenClassUtil openClassSyllabusTableView:CGRectMake(originX, 0, APP_VIEW_WIDTH/2, tableViewHeight) view:self.scrollView theDelegate:self];
    originX = originX + tableViewWidht;
    self.tableView3 = [OpenClassUtil openClassSyllabusTableView:CGRectMake(originX, 0, tableViewWidht, tableViewHeight) view:self.scrollView theDelegate:self];
    originX = originX + tableViewWidht;
    self.tableView4 = [OpenClassUtil openClassSyllabusTableView:CGRectMake(originX, 0, tableViewWidht, tableViewHeight) view:self.scrollView theDelegate:self];
    originX = originX + tableViewWidht;
    self.tableView5 = [OpenClassUtil openClassSyllabusTableView:CGRectMake(originX, 0, tableViewWidht, tableViewHeight) view:self.scrollView theDelegate:self];
    originX = originX + tableViewWidht;
    self.tableView6 = [OpenClassUtil openClassSyllabusTableView:CGRectMake(originX, 0, tableViewWidht, tableViewHeight) view:self.scrollView theDelegate:self];
    originX = originX + tableViewWidht;
    self.tableView7 = [OpenClassUtil openClassSyllabusTableView:CGRectMake(originX, 0, tableViewWidht, tableViewHeight) view:self.scrollView theDelegate:self];
}

#pragma mark - button 点击事件
- (void)btnAction:(UIButton *)button {
    
    if (button.tag == 1000) { //
       
        [self.dataArray removeAllObjects];
        if (self.array1) {
            [self.dataArray addObjectsFromArray:self.array1];
        }
        if (self.array2) {
            [self.dataArray addObjectsFromArray:self.array2];
        }
        if (self.array3) {
            [self.dataArray addObjectsFromArray:self.array3];
        }
        if (self.array4) {
            [self.dataArray addObjectsFromArray:self.array4];
        }
        if (self.array5) {
            [self.dataArray addObjectsFromArray:self.array5];
        }
        if (self.array6) {
            [self.dataArray addObjectsFromArray:self.array6];
        }
        if (self.array7) {
            [self.dataArray addObjectsFromArray:self.array7];
        }
        
        
        NSDictionary *dic = [[NSDictionary alloc] initWithObjectsAndKeys:self.dataArray,@"syllabusData",  nil];
        NSNotification * notice = [NSNotification notificationWithName:@"OpenNewClassSyllabusFinish" object:nil userInfo:dic];
        //发送消息
        [[NSNotificationCenter defaultCenter]postNotification:notice];
        
        [self.navigationController popViewControllerAnimated:YES];
        
    }
    
    
}

// 完成
- (void)setRightBtn {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH-74, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + APP_STATUSBAR_HEIGHT, 64, APP_NAV_LEFT_ITEM_HEIGHT);
    button.tag = 1000;
    button.backgroundColor = [UIColor clearColor];
    [button setTitle:@"完成" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [button.titleLabel setTextAlignment:NSTextAlignmentRight];
    [self setRight:button];
    
    
}


@end
