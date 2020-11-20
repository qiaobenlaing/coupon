//
//  BMSQ_OrderDetailController.m
//  BMSQC
//
//  Created by gh on 15/10/11.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_OrderDetailController.h"
#import "UIButtonEx.h"
#import "BMSQ_PayCardViewController.h"
#import "SVProgressHUD.h"


#import "ZXingObjC.h"
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>
#import "GTMDefines.h"
#import "GTMBase64.h"
#import "JSONKit.h"

#define marginX 10

@interface BMSQ_OrderDetailController () {
    
    UITableView *m_tableView;
    
    UILabel *lb_shopName;
    UILabel *lb_orderNbr;
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
    
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"订单详情"];
    
    isSelect = NO;
    
    m_tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    lb_shopName = [[UILabel alloc] initWithFrame:CGRectMake(marginX, 0, APP_VIEW_WIDTH/3*2, 44)];
    lb_shopName.backgroundColor = [UIColor clearColor];
    lb_shopName.font = [UIFont systemFontOfSize:13];
    lb_shopName.text = @"";
    
    lb_orderNbr = [[UILabel alloc] initWithFrame:CGRectMake(marginX, 0, APP_VIEW_WIDTH/3*2, 30)];
    lb_orderNbr.backgroundColor = [UIColor clearColor];
    lb_orderNbr.font = [UIFont systemFontOfSize:13];
    lb_orderNbr.text = @"";
    
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
    
}


#pragma mark - UITableView delegate 

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0) {
        if (indexPath.row == 0) {
            return 30;
            
        }else if (indexPath.row == 2){
            return 100;
            
        }else if (indexPath.row == 3){
            return 10;
            
        }else if (indexPath.row == 5) {
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
    if (!self.isFinsh) {
        return 2;
    }else{
        return 1;
    }
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section == 1) {
        return 2;
    }
    return 7;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
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
            
            //商家名称
            [cell addSubview:lb_shopName];
            
            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
            
            return cell;
            
        }else if (indexPath.row == 2) {
            
            UITableViewCell *cell = [[UITableViewCell alloc] init];
            cell.backgroundColor = [UIColor whiteColor];
            cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
           //订单号
            [cell.contentView addSubview:lb_orderNbr];
            
            UIImageView *couponCodeQR = [[UIImageView alloc]initWithFrame:CGRectMake(0, lb_orderNbr.frame.origin.y + lb_orderNbr.frame.size.height,APP_VIEW_WIDTH, 0)];
            couponCodeQR.tag = 101;
            [cell.contentView addSubview:couponCodeQR];

            if (m_dic) {
                NSError *error = nil;
                ZXMultiFormatWriter *writer = [ZXMultiFormatWriter writer];
                ZXBitMatrix* result = [writer encode:[m_dic objectForKey:@"orderNbr"]
                                              format:kBarcodeFormatCode128
                                               width:200
                                              height:25
                                               error:&error];
                
                
                if (result) {
                    CGImageRef imageT = [[ZXImage imageWithMatrix:result] cgimage];
                    UIImage* imageF = [UIImage imageWithCGImage: imageT];
                    couponCodeQR.frame = CGRectMake(0, lb_orderNbr.frame.origin.y + lb_orderNbr.frame.size.height,APP_VIEW_WIDTH, 60);
                    couponCodeQR.backgroundColor  = [UIColor clearColor];
                    [couponCodeQR setImage:imageF];
                    [cell addSubview:couponCodeQR];
                    
                }
            }
            
            
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
            
            UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(marginX, 0, APP_VIEW_WIDTH-40, 44)];
            lb_right.backgroundColor = [UIColor clearColor];
            lb_right.font = [UIFont systemFontOfSize:13];
            lb_right.text = @"消费金额";
            [cell addSubview:lb_right];
            [cell addSubview:lb_orderAmount];
            
            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
            
            return cell;
        }else if (indexPath.row == 5) {
            if (!isSelect) {
                
                UITableViewCell *cell = [[UITableViewCell alloc] init];
                cell.backgroundColor = [UIColor whiteColor];
                cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
                UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(marginX, 0, APP_VIEW_WIDTH-40, 44)];
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
                
                UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(marginX, 0, APP_VIEW_WIDTH-40, 44)];
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
                    UILabel *lb_left = [[UILabel alloc] initWithFrame:CGRectMake(20, 44+25*i, APP_VIEW_WIDTH-(20*2), 20)];
                    lb_left.font = [UIFont systemFontOfSize:11];
                    lb_left.textColor = [UIColor darkGrayColor];
                    [cell addSubview:lb_left];
                    
                    UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(20, 44+25*i, APP_VIEW_WIDTH-(20*2), 20)];
                    lb_right.textAlignment = NSTextAlignmentRight;
                    lb_right.backgroundColor = [UIColor clearColor];
                    lb_right.font = [UIFont systemFontOfSize:11];
                    lb_right.textColor = [UIColor darkGrayColor];
                    [cell addSubview:lb_right];
                    
                    
                    if (i == 0) {
                        lb_left.text = @"银行卡折扣 : ";
                        
                        if (m_dic) {
                            lb_right.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"bankCardDeduction"]];
                        }
                        
                    }else if (i == 1) {
                        lb_left.text = @"会员卡 : ";
                        if (m_dic) {
                            lb_right.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"cardDeduction"]];
                        }
                        
                    }else if (i == 2) {
                        
                        
                        if (m_dic) {
                            NSString *type = [m_dic objectForKey:@"couponType"];
                            if (type.intValue == 1){
                                if ([[m_dic objectForKey:@"function"]  isEqual: @""]) {
                                    lb_left.text = @"N元购 : ";
                                    
                                }else {
                                    lb_left.text = [m_dic objectForKey:@"function"];
                                    
                                }
                                
                            }else if (type.intValue == 3 || type.intValue == 32||type.intValue == 33){
                                lb_left.text = [NSString stringWithFormat:@"抵扣券 : 满%@元立减%@元",[m_dic objectForKey:@"availablePrice"],[m_dic objectForKey:@"insteadPrice"]];
                                
                            } else if (type.intValue == 4){
                                lb_left.text = [NSString stringWithFormat:@"折扣券 : 满%@元打%0.1f折",[m_dic objectForKey:@"availablePrice"],[[m_dic objectForKey:@"discountPercent"] floatValue]];
                                
                            } else if (type.intValue == 5 ||type.intValue == 6){
                                lb_left.text = [NSString stringWithFormat:@"%@",[m_dic objectForKey:@"function"]];
                                lb_right.hidden = YES;
                                
                            }else {
                                lb_left.text = @"抵扣券或折扣券 :";
                                
                            }

                            lb_right.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"couponDeduction"]];
                            
                        }
                        
                    }else if (i == 3) {
                        lb_left.text = @"商家红包 :";
                        if (m_dic) {
                            lb_right.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"shopBonusDeduction"]];
                            
                        }
                    }else if (i == 4) {
                        lb_left.text = @"惠圈红包 :";
                        if (m_dic) {
                            lb_right.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"platBonusDeduction"]];
                            
                        }
                    }
                }
                
                UIView *line1 = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5+25*5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
                line1.backgroundColor = APP_CELL_LINE_COLOR;
                [cell addSubview:line1];
                
                return cell;
            }
            
        }else if (indexPath.row == 6) {
           
            UITableViewCell *cell = [[UITableViewCell alloc] init];
            cell.backgroundColor = [UIColor whiteColor];
            cell.selectionStyle=UITableViewCellSelectionStyleNone;
            
            UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(marginX, 0, APP_VIEW_WIDTH-40, 44)];
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

    }else if (indexPath.section == 1) {
        static NSString *cellIdentifier = @"payCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        
        if (cell == nil)
        {
            
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        }
        
        if (indexPath.row == 0) {
            UILabel *lb_pay = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20, 40)];
            lb_pay.backgroundColor = UICOLOR(182, 0, 12, 1.0);
            lb_pay.text = @"工行卡支付";
            lb_pay.layer.cornerRadius = 3;
            lb_pay.layer.masksToBounds = YES;
            lb_pay.textAlignment = NSTextAlignmentCenter;
            lb_pay.textColor = [UIColor whiteColor];
            [cell addSubview:lb_pay];
            
            UIButtonEx *btn_pay = [[UIButtonEx alloc] initWithFrame:CGRectMake(20, 10, APP_VIEW_WIDTH-40, 40)];
            btn_pay.tag = 1001;
            btn_pay.object = [m_dic objectForKey:@"orderCode"];
            btn_pay.backgroundColor = [UIColor clearColor];
            [btn_pay addTarget:self action:@selector(payClick:) forControlEvents:UIControlEventTouchUpInside];
            [cell addSubview:btn_pay];
        }
        else{
            UILabel *lb_cancelPay = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20, 40)];
            lb_cancelPay.backgroundColor = [UIColor darkGrayColor];
            lb_cancelPay.text = @"取消支付";
            lb_cancelPay.layer.masksToBounds = YES;
            lb_cancelPay.layer.cornerRadius = 3;
            lb_cancelPay.textAlignment = NSTextAlignmentCenter;
            lb_cancelPay.textColor = [UIColor whiteColor];
            [cell addSubview:lb_cancelPay];
            
            UIButtonEx *btn_cancelPay = [[UIButtonEx alloc] initWithFrame:CGRectMake(20, 10, APP_VIEW_WIDTH-40, 40)];
            self.object = [m_dic objectForKey:@"orderCode"];
            btn_cancelPay.tag = 1000;
            btn_cancelPay.backgroundColor = [UIColor clearColor];
            [btn_cancelPay addTarget:self action:@selector(payClick:) forControlEvents:UIControlEventTouchUpInside];
            [cell addSubview:btn_cancelPay];
        }
        
        
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle=UITableViewCellSelectionStyleNone;
        
        
        return cell;

    }
    
    
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    

    if (indexPath.row == 5) {
        isSelect = !isSelect;
        
        [m_tableView reloadRowsAtIndexPaths:@[indexPath]
                           withRowAnimation:UITableViewRowAnimationFade];
    }
    

}


- (void)payClick:(UIButtonEx *)button{
    
    if (button.tag == 1001) {//1001 工行卡支付  1000取消支付
        

        BMSQ_PayCardViewController *vc = [[BMSQ_PayCardViewController alloc]init];
        vc.fromVC = (int) self.navigationController.viewControllers.count;
        vc.payNewPrice = [self.couponDic objectForKey:@"realPay"];  //花费钱数
        vc.payType = @"1";   //支付方式
        vc.shopName = [m_dic objectForKey:@"shopName"];
        vc.shopCode = [m_dic objectForKey:@"shopCode"];
        vc.orderNbr = [m_dic objectForKey:@"orderNbr"];
        vc.consumeCode = self.consumeCode;
        vc.type56 = YES;
        [self.navigationController pushViewController:vc animated:YES];
        
        
    }
    
    else if (button.tag == 1000) {
        
        UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:@"确认取消支付?" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        alert.tag = 100;
        [alert show];
        
    }
    
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 100 && buttonIndex == 1)
    {
        [self cancelOrder:self.object];
        NSLog(@"取消支付");
        
    }
    else if(alertView.tag == 101 && buttonIndex == 1)
    {
        
    }
}





//订单详情
- (void)getOrderDetails
{
    
    [self initJsonPrcClient:@"2"];
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
            lb_shopName.text = [m_dic objectForKey:@"shopName"];
            lb_orderNbr.text = [NSString stringWithFormat:@"订单号：%@",[m_dic objectForKey:@"orderNbr"]];
            lb_orderAmount.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"orderAmount"]];
            lb_deduction.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"deduction"]];
            lb_realPay.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"realPay"]];
            
        }
        
        [m_tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
    
}


//取消支付
- (void)cancelOrder:(NSObject*)object {
    
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.consumeCode forKey:@"consumeCode"];
    
    NSString* vcode = [gloabFunction getSign:@"cancelBankcardPay" strParams:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak __typeof(self)weakSelf = self;
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"cancelBankcardPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        
        if ([[responseObject objectForKey:@"code"] isEqual:@50000]) {
            
            [SVProgressHUD showSuccessWithStatus:@"取消订单成功"];
            
            
        }
        else if ([[responseObject objectForKey:@"code"] isEqual:@20000])
        {
            NSLog(@"取消失败");
            
        }
        [[NSNotificationCenter defaultCenter]postNotificationName:@"freshNorderList" object:nil];
        [weakSelf.navigationController popViewControllerAnimated:YES];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD showErrorWithStatus:@"取消订单失败"];
        [weakSelf.navigationController popViewControllerAnimated:YES];
        
    }];
    
}













@end
