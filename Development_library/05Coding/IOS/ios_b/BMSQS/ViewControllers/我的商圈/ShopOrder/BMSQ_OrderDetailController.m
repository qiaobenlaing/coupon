//
//  BMSQ_OrderDetailController.m
//  BMSQC
//
//  Created by gh on 15/10/11.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_OrderDetailController.h"
#import "UIButtonEx.h"
#import "SVProgressHUD.h"

@interface BMSQ_OrderDetailController () {
    
    UITableView *m_tableView;
    
    UILabel *lb_userName;
    UILabel *lb_orderNbr;
    UILabel *lb_orderType;
    UILabel *lb_orderAmount;
    UILabel *lb_deduction;
    UILabel *lb_realPay;
    
    NSMutableDictionary *m_dic;
    
    BOOL isSelect;
    
}


@end

@implementation BMSQ_OrderDetailController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self getOrderDetails];

    [self setViewUp];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setViewUp {
    
    [self.navigationController setNavigationBarHidden:NO];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    [self.navigationItem setTitle:@"订单管理"];
    
    isSelect = NO;
    
    m_tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    lb_userName = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-40, 44)];
    lb_userName.backgroundColor = [UIColor clearColor];
    lb_userName.font = [UIFont systemFontOfSize:13];
    lb_userName.text = @"用户名";
    
    lb_orderNbr = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-40, 44)];
    lb_orderNbr.backgroundColor = [UIColor clearColor];
    lb_orderNbr.font = [UIFont systemFontOfSize:13];
    lb_orderNbr.text = @"订单号：";
    
    lb_orderType = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3*2, 44)];
    lb_orderType.backgroundColor = [UIColor clearColor];
    lb_orderType.textColor = [UIColor lightGrayColor];
    lb_orderType.font = [UIFont systemFontOfSize:13];
    lb_orderType.text = @"";
    
    lb_orderAmount = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2-20, 44)];
    lb_orderAmount.backgroundColor = [UIColor clearColor];
    lb_orderAmount.textAlignment = NSTextAlignmentRight;
    lb_orderAmount.font = [UIFont systemFontOfSize:13];
    lb_orderAmount.text = @"0.00";
    
    lb_deduction = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2-20, 44)];
    lb_deduction.backgroundColor = [UIColor clearColor];
    lb_deduction.textAlignment = NSTextAlignmentRight;
    lb_deduction.font = [UIFont systemFontOfSize:13];
    lb_deduction.text = @"0.00";
    
    lb_realPay= [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2-20, 44)];
    lb_realPay.backgroundColor = [UIColor clearColor];
    lb_realPay.textAlignment = NSTextAlignmentRight;
    lb_realPay.textColor = [UIColor redColor];
    lb_realPay.font = [UIFont systemFontOfSize:13];
    lb_realPay.text = @"0.00";
    

    
    lb_userName.text = [_couponDic objectForKey:@"nickName"];
//    lb_orderNbr.text = [_couponDic objectForKey:@"orderNbr"];
//    lb_orderAmount.text = [NSString stringWithFormat:@"%@元",[_couponDic objectForKey:@"orderAmount"]];
//    lb_deduction.text = [NSString stringWithFormat:@"%@元",[_couponDic objectForKey:@"deduction"]];
//    lb_realPay.text = [NSString stringWithFormat:@"%@元",[_couponDic objectForKey:@"realPay"]];
    
}


#pragma mark - UITableView delegate 

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0) {
        if (indexPath.row == 0) {
            return 30;
        }else if (indexPath.row == 3){
            return 10;
        }else if (indexPath.row == 6) {
            if (!isSelect) {
                return 44;
            }else{
                return 44+25*5;
            }
        }
    }
    return 44;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return 8;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    CGFloat x = 10;
    
    if (indexPath.section == 0) {
        if (indexPath.row == 0) {
            
            UITableViewCell *cell = [[UITableViewCell alloc] init];
            cell.backgroundColor = APP_VIEW_BACKCOLOR;
            cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
            UILabel *lb_consumeTime = [[UILabel alloc] initWithFrame:CGRectMake(20, 0, APP_VIEW_WIDTH-40, 30)];
            lb_consumeTime.textAlignment = NSTextAlignmentRight;
            lb_consumeTime.textColor = [UIColor darkGrayColor];
            lb_consumeTime.font = [UIFont systemFontOfSize:10];
            if (m_dic) {
                lb_consumeTime.text = [m_dic objectForKey:@"consumeTime"];
            }
            [cell    addSubview:lb_consumeTime];
            return cell;
            
        }else if (indexPath.row == 1) {
            
            UITableViewCell *cell = [[UITableViewCell alloc] init];
            cell.backgroundColor = [UIColor whiteColor];
            cell.selectionStyle=UITableViewCellSelectionStyleNone;

//            UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH-40, 44)];
//            lb_right.backgroundColor = [UIColor clearColor];
//            lb_right.font = [UIFont systemFontOfSize:13];
//            lb_right.text = @"用户名称";
//            [cell addSubview:lb_right];
            [cell addSubview:lb_userName];
            
            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
            
            return cell;
            
        }else if (indexPath.row == 2) {
            
            UITableViewCell *cell = [[UITableViewCell alloc] init];
            cell.backgroundColor = [UIColor whiteColor];
            cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
//            UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH-40, 44)];
//            lb_right.backgroundColor = [UIColor clearColor];
//            lb_right.font = [UIFont systemFontOfSize:13];
//            lb_right.text = @"订单号";
//            [cell addSubview:lb_right];
            [cell addSubview:lb_orderNbr];
            
            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
            
            return cell;
            
        }else if (indexPath.row == 3) {
            
            UITableViewCell *cell = [[UITableViewCell alloc] init];
            cell.backgroundColor = APP_VIEW_BACKCOLOR;
            cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
            return cell;
            
        }else if (indexPath.row == 4) {
            
            UITableViewCell *cell = [[UITableViewCell alloc] init];
            cell.backgroundColor = [UIColor whiteColor];
            cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
            UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH-40, 44)];
            lb_right.backgroundColor = [UIColor clearColor];
            lb_right.font = [UIFont systemFontOfSize:13];
            lb_right.text = @"订单状态";
            [cell addSubview:lb_right];
            [cell addSubview:lb_orderType];
            
            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
            
            return cell;
        }else if (indexPath.row == 5) {
            
            UITableViewCell *cell = [[UITableViewCell alloc] init];
            cell.backgroundColor = [UIColor whiteColor];
            cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
            UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH-40, 44)];
            lb_right.backgroundColor = [UIColor clearColor];
            lb_right.font = [UIFont systemFontOfSize:13];
            lb_right.text = @"消费金额";
            [cell addSubview:lb_right];
            [cell addSubview:lb_orderAmount];
            
            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
            
            return cell;
        }else if (indexPath.row == 6) {
            if (!isSelect) {
                
                UITableViewCell *cell = [[UITableViewCell alloc] init];
                cell.backgroundColor = [UIColor whiteColor];
                cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
                UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH-40, 44)];
                lb_right.backgroundColor = [UIColor clearColor];
                lb_right.font = [UIFont systemFontOfSize:13];
                lb_right.text = @"优惠金额";
                [cell addSubview:lb_right];
                [cell addSubview:lb_deduction];
                
                UIImageView *iv_arrow = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-20, 19, 10, 7)];
                iv_arrow.backgroundColor = [UIColor clearColor];
                iv_arrow.image = [UIImage imageNamed:@"iv_arrowBottomHeight"];
                [cell addSubview:iv_arrow];
                
                UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
                line.backgroundColor = APP_CELL_LINE_COLOR;
                [cell addSubview:line];
            
                return cell;
            }else {
                UITableViewCell *cell = [[UITableViewCell alloc] init];
                cell.backgroundColor = [UIColor whiteColor];
                cell.selectionStyle=UITableViewCellSelectionStyleNone;
                
                UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH-40, 44)];
                lb_right.backgroundColor = [UIColor clearColor];
                lb_right.font = [UIFont systemFontOfSize:13];
                lb_right.text = @"优惠金额";
                [cell addSubview:lb_right];
                [cell addSubview:lb_deduction];
                
                
                UIImageView *iv_arrow = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-20, 19, 10, 7)];
                iv_arrow.backgroundColor = [UIColor clearColor];
                iv_arrow.image = [UIImage imageNamed:@"iv_arrowTopHeight"];
                [cell addSubview:iv_arrow];
                
                UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
                line.backgroundColor = APP_CELL_LINE_COLOR;
                [cell addSubview:line];

                
                for (int i=0; i<5; i++) {
                    UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(20, 44+25*i, APP_VIEW_WIDTH-(20*2), 20)];
                    lb_right.font = [UIFont systemFontOfSize:11];
                    lb_right.textColor = [UIColor darkGrayColor];
                    [cell addSubview:lb_right];
                    
                    UILabel *lb_left = [[UILabel alloc] initWithFrame:CGRectMake(20, 44+25*i, APP_VIEW_WIDTH-(20*2), 20)];
                    lb_left.textAlignment = NSTextAlignmentRight;
                    lb_left.backgroundColor = [UIColor clearColor];
                    lb_left.font = [UIFont systemFontOfSize:11];
                    lb_left.textColor = [UIColor darkGrayColor];
                    [cell addSubview:lb_left];
                    
                    
                    if (i == 0) {
                        lb_right.text = @"银行卡折扣 : ";
                        if (m_dic) {
                            lb_left.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"bankCardDeduction"]];
                        }
                        
                    }else if (i == 1) {
                        lb_right.text = @"会员卡 : ";
                        if (m_dic) {
                            lb_left.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"cardDeduction"]];
                        }
                        
                        
                    }else if (i == 2) {
                        if (m_dic) {
                            NSString *type = [m_dic objectForKey:@"couponType"];
                         
                            if (type.intValue == 1){
                                
                                if ([[m_dic objectForKey:@"function"]  isEqual: @""]) {
                                    lb_right.text = @"N元购 : ";
                                }else {
                                    lb_right.text = [m_dic objectForKey:@"function"];
                                }
                                
                            }else if (type.intValue == 3){
                                
                                lb_right.text = [NSString stringWithFormat:@"抵扣券 : 满%@元立减%@元",[m_dic objectForKey:@"availablePrice"],[m_dic objectForKey:@"insteadPrice"]];
                            } else if (type.intValue == 4){
                                
                                lb_right.text = [NSString stringWithFormat:@"折扣券 : 满%@元打%0.1f折",[m_dic objectForKey:@"availablePrice"],[[m_dic objectForKey:@"discountPercent"] floatValue]];
                            } else if (type.intValue == 5 ||type.intValue == 6)
                            {
                                lb_right.text = [NSString stringWithFormat:@"%@",[m_dic objectForKey:@"function"]];
                                lb_left.hidden = YES;
                            }else {
                                lb_right.text = @"抵扣券或折扣券 :";
                            }
                            
                            lb_left.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"couponDeduction"]];
                        }
                        
                    }else if (i == 3) {
                        lb_right.text = @"商家红包 :";
                        if (m_dic) {
                            lb_left.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"shopBonusDeduction"]];
                        }
                    }else if (i == 4) {
                        lb_right.text = @"惠圈红包 :";
                        if (m_dic) {
                            lb_left.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"platBonusDeduction"]];
                        }
                    }
                    
                    
                }
                
                UIView *line1 = [[UIView alloc] initWithFrame:CGRectMake(0, 44+25*5-0.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
                line1.backgroundColor = APP_CELL_LINE_COLOR;
                [cell addSubview:line1];
                
                return cell;
            }
            
        }else if (indexPath.row == 7) {
           
            UITableViewCell *cell = [[UITableViewCell alloc] init];
            cell.backgroundColor = [UIColor whiteColor];
            cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
            UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH-40, 44)];
            lb_right.backgroundColor = [UIColor clearColor];
            lb_right.font = [UIFont boldSystemFontOfSize:14];
            lb_right.text = @"实际支付";
            [cell addSubview:lb_right];
            [cell addSubview:lb_realPay];
            
            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
            
            return cell;
        }

    }
    
    
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    

    if (indexPath.row == 6) {
        isSelect = !isSelect;
        
        [m_tableView reloadRowsAtIndexPaths:@[indexPath]
                           withRowAnimation:UITableViewRowAnimationFade];
    }
    

}


//订单详情
- (void)getOrderDetails
{
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.consumeCode forKey:@"consumeCode"];
    NSString* vcode = [gloabFunction getSign:@"getConsumeInfo" strParams:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getConsumeInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        
        m_dic = responseObject;
        
        if (m_dic) {
            lb_userName.text = [_couponDic objectForKey:@"nickName"];
            lb_orderNbr.text = [NSString stringWithFormat:@"订单号：%@",[m_dic objectForKey:@"orderNbr"]];
            lb_orderAmount.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"orderAmount"]];
            lb_deduction.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"deduction"]];
            lb_realPay.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"realPay"]];
            
            NSNumber *num = [_couponDic objectForKey:@"userConsumeStatus"];
            switch (num.intValue) {
                case 1:
                    lb_orderType.text = @"未付款";
                    break;
                case 2:
                    lb_orderType.text = @"付款中";
                    break;
                case 3:
                    lb_orderType.text = @"已付款";
                    break;
                case 4:
                    lb_orderType.text = @"已取消付款";
                    break;
                case 5:
                    lb_orderType.text = @"付款失败";
                    break;
                default:
                    break;
            }
            
        }
        
        [m_tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
    
}



@end
