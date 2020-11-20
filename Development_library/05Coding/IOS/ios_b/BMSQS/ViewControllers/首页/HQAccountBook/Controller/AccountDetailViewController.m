//
//  AccountDetailViewController.m
//  BMSQS
//
//  Created by gh on 16/3/3.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "AccountDetailViewController.h"
#import "ZXingObjC.h"
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>
#import "GTMDefines.h"
#import "GTMBase64.h"
#import "JSONKit.h"

#define marginX 10

@interface AccountDetailViewController ()<UITableViewDataSource, UITableViewDelegate>
{
    
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

@implementation AccountDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
    
}

- (void)setViewUp {
    
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"订单详情"];
    
    [self getOrderDetails];
    isSelect = YES;
    
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
    return 1;
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
                            NSString *leftString = [self getLeftString];
                            
                            lb_left.text = leftString;
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
        
    }
    
    
    return nil;
}

- (NSString *)getLeftString {
    NSString *type = [m_dic objectForKey:@"couponType"];
    NSString *leftString = @"";
    switch (type.intValue) {
        case 1:
            if ([[m_dic objectForKey:@"function"]  isEqual: @""]) {
                leftString = @"N元购 : ";
                
            }else {
                leftString = [m_dic objectForKey:@"function"];
                
            }
            break;
        case 3:
            leftString = [NSString stringWithFormat:@"抵扣券 : 满%@元立减%@元",[m_dic objectForKey:@"availablePrice"],[m_dic objectForKey:@"insteadPrice"]];
            break;
            
        case 32:
            leftString = [NSString stringWithFormat:@"抵扣券 : 满%@元立减%@元",[m_dic objectForKey:@"availablePrice"],[m_dic objectForKey:@"insteadPrice"]];
            break;
            
        case 33:
            leftString = [NSString stringWithFormat:@"抵扣券 : 满%@元立减%@元",[m_dic objectForKey:@"availablePrice"],[m_dic objectForKey:@"insteadPrice"]];
            break;
        case 4:
            leftString = [NSString stringWithFormat:@"折扣券 : 满%@元打%0.1f折",[m_dic objectForKey:@"availablePrice"],[[m_dic objectForKey:@"discountPercent"] floatValue]];
            break;
//        case 5:
//            leftString = [NSString stringWithFormat:@"%@",[m_dic objectForKey:@"function"]];
//            break;
//        case 6:
//            leftString = [NSString stringWithFormat:@"%@",[m_dic objectForKey:@"function"]];
//            break;
//        case 7:
//            leftString = [NSString stringWithFormat:@"%@",[m_dic objectForKey:@"function"]];
//            break;
//        case 8:
//            leftString = [NSString stringWithFormat:@"%@",[m_dic objectForKey:@"function"]];
//            break;
            
        default:
            leftString = @"优惠券";
            break;
    }
    
    return leftString;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    if (indexPath.row == 5) {
        isSelect = !isSelect;
        
        [m_tableView reloadRowsAtIndexPaths:@[indexPath]
                           withRowAnimation:UITableViewRowAnimationFade];
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





@end
