//
//  RefundListViewController.m
//  BMSQS
//
//  Created by gh on 16/3/11.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "RefundListViewController.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"
#import "RefundListCell.h"
#import "UIImageView+WebCache.h"
#import "OpenClassUtil.h"
#import "UIButtonEx.h"
#import "MD5.h"
#import "DCPaymentView.h"

#import "UIColor+Tools.h"

//选取银行卡列表
#import "RefundBackCardViewController.h"



@interface RefundListViewController () <UITableViewDataSource, UITableViewDelegate, refundDelegate, UITextViewDelegate, UIAlertViewDelegate, DCPaymentDelegate>

{
    int page;
}
@property (nonatomic, strong)UIView *bottomView;
@property (nonatomic, strong)UITextView *explainView;
@property (nonatomic, strong)UILabel *textViewPlaceholder;
@property (nonatomic, strong)UIButton *ensureBut;
@property (nonatomic, strong)UIButton *cancelBut;

@property (nonatomic, strong)UITableView *tableView;
@property (nonatomic, strong)NSMutableArray *mdata;


@property (nonatomic, strong)NSDictionary *refundDictionary;
@property (nonatomic, strong)NSString *refundCode;
@property (nonatomic, strong)NSString *refundOrderCode;

@property (nonatomic, assign)int btnForRow; //点击的按钮在第几行

@property (nonatomic, strong)NSMutableArray *backCardArray;
@property (nonatomic, strong)NSDictionary *backCardDic;

@property (nonatomic, strong)NSString *ifShopSetPayPwd;


@end

@implementation RefundListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [self setViewUp];
    
}

- (void)setViewUp {
    page = 1;
    
    //注册选择银行卡通知
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(selectBackCardFinsh:) name:@"selectBackCardFinsh" object:nil];
    
    [self setNavBackItem];
    [self setNavTitle:@"退款处理"];
    
    self.refundCode = @"";
    self.refundOrderCode = @"";
    self.mdata = [[NSMutableArray alloc] init];
    self.backCardArray = [[NSMutableArray alloc] init];
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.backgroundColor = UICOLOR(240, 239, 245, 1);
    [self.view  addSubview:self.tableView];
    
    [self.tableView addHeaderWithTarget:self action:@selector(headerRefresh)];
    [self.tableView addFooterWithTarget:self action:@selector(footerRefresh)];
    
    [self setmyBottomView];

    [self getRefundApplicationList];
    [self getShopCardList];
    [self httpIfShopSetPayPwd];//是否设置了密码
}


- (void)setmyBottomView {
    self.bottomView = [[UIView alloc] initWithFrame:CGRectMake(0, self.view.frame.size.height - 100, APP_VIEW_WIDTH, 100)];
    self.bottomView.hidden = YES;
    self.bottomView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.bottomView];
    [self.view bringSubviewToFront:self.bottomView];
    
    self.explainView = [[UITextView alloc] initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH - 80, 80)];
    self.explainView.delegate = self;
    self.explainView.layer.borderColor = [UIColor grayColor].CGColor;
    self.explainView.layer.borderWidth =1.0;
    self.explainView.layer.cornerRadius =5.0;
    self.explainView.backgroundColor = [UIColor whiteColor];
    [self.bottomView addSubview:self.explainView];
    
    self.textViewPlaceholder = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 15)];
    self.textViewPlaceholder.text = @"请输入不同意理由";
    self.textViewPlaceholder.font = [UIFont systemFontOfSize:12.f];
    self.textViewPlaceholder.textColor = UICOLOR(205, 205, 209, 1.0);
    [self.explainView addSubview:self.textViewPlaceholder];
    
    self.ensureBut = [UIButton buttonWithType:UIButtonTypeCustom];
    self.ensureBut.frame = CGRectMake(APP_VIEW_WIDTH - 65, 20, 60, 30);
    self.ensureBut.backgroundColor = [UIColor grayColor];
    [self.ensureBut addTarget:self action:@selector(ensureBuClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.ensureBut setTitle:@"确定" forState:UIControlStateNormal];
    [self.bottomView addSubview:self.ensureBut];
    
    
    self.cancelBut = [UIButton buttonWithType:UIButtonTypeCustom];
    self.cancelBut.frame = CGRectMake(APP_VIEW_WIDTH - 65, 60, 60, 30);
    self.cancelBut.backgroundColor = [UIColor grayColor];
    [self.cancelBut addTarget:self action:@selector(cancelBuClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.cancelBut setTitle:@"取消" forState:UIControlStateNormal];
    [self.bottomView addSubview:self.cancelBut];

    
    
}

- (void)headerRefresh {

    page = 1;
    [self.tableView addFooterWithTarget:self action:@selector(footerRefresh)];
    [self getRefundApplicationList];
    
}

- (void)footerRefresh {
    page ++;
    [self getRefundApplicationList];
    
}


#pragma mark - UITableView deleagate

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *dic = [self.mdata  objectAtIndex:indexPath.row];
    NSString *infoStr = [NSString stringWithFormat:@"理由:%@", [dic objectForKey:@"refundReason"]];
    CGSize size = [OpenClassUtil getRefundReasonSize:infoStr];
    if (size.height>20) {
        return (20/2-13.0/2)+size.height+230;
    }
    
    return 230+20;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.mdata.count;

}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString* identifier = @"RefundListCell";
    RefundListCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (!cell) {
        cell = [[RefundListCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.backgroundColor = UICOLOR(255, 255, 255, 1);
    cell.refDelegate = self;
    
    NSDictionary *dic = [self.mdata objectAtIndex:indexPath.row];
    [cell setCellValue:dic];
    
    [cell.userIv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, [dic objectForKey:@"avatarUrl"]]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        [cell setNeedsLayout];
        
    }];
    
    cell.leftBtn.tag = 100+indexPath.row;
    cell.rightBtn.tag = 200+indexPath.row;
    
    return cell;
}


#pragma mark - refund delegate
//同意退款
- (void)clickAgreeBnt:(UIButtonEx *)RightBtn {
    NSLog(@"--->>>同意退款");
    
    self.refundDictionary = RightBtn.object;
    self.refundCode = [NSString stringWithFormat:@"%@", [RightBtn.object objectForKey:@"refundCode"]];
    self.btnForRow = (int)RightBtn.tag - 200;
    [self agreeRefund];
}

#pragma mark - 选择银行卡
- (void)selectBackCard {
    NSLog(@"选择银行卡");
    
    RefundBackCardViewController *vc = [[RefundBackCardViewController alloc] init];
    vc.dataAry = self.backCardArray;
    [self.navigationController pushViewController:vc animated:YES];

}
#pragma mark - 获取验证码
- (void)selectCode {
    NSLog(@"获取验证码");
    [self getIcbcMsgValCode];
    
}



#pragma mark - 通知 选择银行卡完成
- (void)selectBackCardFinsh:(NSNotification *)notification {
    self.backCardDic = notification.object;
    DCPaymentView *payAlert = [self.view viewWithTag:9000];
    payAlert.cardMessage = [self.backCardDic objectForKey:@"accountNbrLast4"];
    
}


#pragma mark - 不同意退款
- (void)clickDisagreeBtn:(UIButtonEx *)leftBtn {
    self.btnForRow = (int)leftBtn.tag-100;
    
    self.refundCode = [leftBtn.object objectForKey:@"refundCode"];
    self.bottomView.hidden = NO;
   
    
    NSLog(@"--->>>不同意退款");
}




#pragma mark - 不同意退款确定
- (void)ensureBuClick:(UIButton *)sender
{
    if (self.explainView.text.length == 0) {
        CSAlert(@"请输入理由");
        return;
    }
    [self shopRejectRefund];
    
}


#pragma mark - 不同意退款取消
- (void)cancelBuClick:(UIButton *)sender
{
    self.bottomView.hidden = YES;
    self.textViewPlaceholder.hidden = NO;
    self.bottomView.frame = CGRectMake(0, self.view.frame.size.height - 100, APP_VIEW_WIDTH, 100);
    self.explainView.text = @"";
    [self textViewDidEndEditing:self.explainView];
}

#pragma mark ------ UITextViewDelegate
- (void)textViewDidBeginEditing:(UITextView *)textView
{
    self.textViewPlaceholder.hidden = YES;
    self.bottomView.frame = CGRectMake(0, self.view.frame.size.height - 356, APP_VIEW_WIDTH, 100);
}
- (void)textViewDidEndEditing:(UITextView *)textView
{
    [textView resignFirstResponder];
}


#pragma mark - UIAlertView delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex NS_DEPRECATED_IOS(2_0, 9_0)
{
    if  (alertView.tag == 55004) {
        if  (buttonIndex == 1) { //@"确认"
            if ([self.backCardDic count] == 0) {
                CSAlert(@"您没有绑定银行卡,请前往银行卡管理绑定银行卡");
                return;
            }
            
            
            NSString *mobileNum = [NSString stringWithFormat:@"%@", [self.refundDictionary objectForKey:@"mobileNbr"]];
            NSString *price = [NSString stringWithFormat:@"%@",[self.refundDictionary objectForKey:@"realPay"]];
            
            NSString *cardMessage = [NSString stringWithFormat:@"%@",[self.backCardDic objectForKey:@"accountNbrLast4"]];
            
            if (price.intValue < 300) {
#pragma mark - 300 以下 密码支付
                if ([self.ifShopSetPayPwd isEqualToString:@"1"]) {
                    DCPaymentView *payAlert = [[DCPaymentView alloc]init];
                    payAlert.tag = 9000;
                    payAlert.title = @"请输入支付密码";
                    payAlert.mobileNum = mobileNum;
                    payAlert.amount = [price floatValue];
                    payAlert.cardMessage = cardMessage;
                    [payAlert showInTheView:self.view isCode:NO];
                    payAlert.DCdelegate = self;
                    payAlert.completeHandle = ^(NSString *inputPwd) {

                        [self payForRefund:inputPwd isCode:NO];
                        
                        
                    };
                }else {
                    DCPaymentView *payAlert = [[DCPaymentView alloc]init];
                    payAlert.tag = 9000;
                    payAlert.title = @"请输入短信验证码";
                    payAlert.mobileNum = mobileNum;
                    payAlert.amount = [price floatValue];
                    payAlert.cardMessage = cardMessage;
                    [payAlert showInTheView:self.view isCode:YES];
                    payAlert.DCdelegate = self;
                    payAlert.completeHandle = ^(NSString *inputPwd) {
                        //                    NSLog(@"密码是%@",inputPwd);
                        [self payForRefund:inputPwd isCode:YES] ;
                        
                        
                    };
                }
                
            }else  {
#pragma mark - 300 以上验证码支付
                DCPaymentView *payAlert = [[DCPaymentView alloc]init];
                payAlert.tag = 9000;
                payAlert.title = @"请输入短信验证码";
                payAlert.mobileNum = mobileNum;
                payAlert.amount = [price floatValue];
                payAlert.cardMessage = cardMessage;
                [payAlert showInTheView:self.view isCode:YES];
                payAlert.DCdelegate = self;
                payAlert.completeHandle = ^(NSString *inputPwd) {
                    //                    NSLog(@"密码是%@",inputPwd);
                    [self payForRefund:inputPwd isCode:YES];
                    
                    
                };

            }

        }
    }
    
}

#pragma mark - 同意退款
- (void)agreeRefund {
    [SVProgressHUD showWithStatus:@"正在处理" maskType:SVProgressHUDMaskTypeClear];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:self.refundCode forKey:@"refundCode"];
    [params setObject:self.refundOrderCode forKey:@"refundOrderCode"];
    
    NSString* vcode = [gloabFunction getSign:@"agreeRefund" strParams:self.refundCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [self.jsonPrcClient invokeMethod:@"agreeRefund" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        self.refundOrderCode = @"";
        NSLog(@"%@",responseObject);
        
        NSString *codeStr =[responseObject objectForKey:@"code"];
        if (codeStr.intValue == 50000) {////test
            //            NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:[self.mdata objectAtIndex:self.btnForRow]];
            //            [self.mdata removeObjectAtIndex:self.btnForRow];
            //            [dic setObject:@"2" forKey:@"handleFlag"];
            //            [self.mdata insertObject:dic atIndex:self.btnForRow];
            //
            //            [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:[NSIndexPath indexPathForRow:self.btnForRow inSection:0], nil] withRowAnimation:UITableViewRowAnimationNone];
            page = 1;
            [self getRefundApplicationList];
            [SVProgressHUD showSuccessWithStatus:@"退款成功"];
        }
        
        switch (codeStr.intValue) {
            case 20000:
                CSAlert(@"失败");
                break;
            case 55004: {//(@"不能直接退款，需要商家打款");
                
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"提示" message:@"不能直接退款，需要打款" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确认", nil];
                alertView.tag = 55004;
                [alertView show];
            }
                break;
                
            case 55006:
                CSAlert(@"商家没有对退款进行打款");
                break;
                
            default:
                break;
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
    
    
}



#pragma mark - 商家打款
-(void)payForRefund:(NSString *)nums isCode:(BOOL)isCode{

//    bankAccountCode	银行账户编码
//    payPwd	支付密码	string	md5加密。没有值时送空串
//    refuncCode	退款编码		第一个参数
//    valCode	短信验证码
    [SVProgressHUD showWithStatus:@"正在支付" maskType:SVProgressHUDMaskTypeClear];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
   
    
    [params setObject:self.refundCode forKey:@"refundCode"];//退款编码
    [params setObject:[self.backCardDic objectForKey:@"bankAccountCode"] forKey:@"bankAccountCode"];
    if (isCode) { //验证码支付
        [params setObject:@"" forKey:@"payPwd"];
        [params setObject:nums forKey:@"valCode"];
    }else { //密码支付
         NSString *payPwd = [MD5 MD5Value:nums];
        [params setObject:payPwd forKey:@"payPwd"];//payPwd	支付密码	string	md5加密。没有值时送空串
        [params setObject:@"" forKey:@"valCode"];
    }

    NSString* vcode = [gloabFunction getSign:@"payForRefund" strParams:self.refundCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf =  self;
    [self.jsonPrcClient invokeMethod:@"payForRefund" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSString *resut = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        
//        20000-失败；50000-成功；-；-;-;BXXXX-为银行返回的错误代码
        if (resut.intValue == 50000) {
            self.refundOrderCode = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"orderCode"]];
//            [SVProgressHUD showSuccessWithStatus:@"退款成功"];
            [self agreeRefund];
            
        }else{
            NSString *errorStr = [NSString stringWithFormat:@"%@", [responseObject objectForKey:@"retMsg"]];
            CSAlert(errorStr);
            
            [[NSNotificationCenter defaultCenter]postNotificationName:@"removeAllNum" object:nil];;
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
}



#pragma mark - 不同意退款
- (void)shopRejectRefund {
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    

    [params setObject:self.refundCode forKey:@"refundCode"];
    [params setObject:self.explainView.text forKey:@"rejectReason"];
    
    NSString* vcode = [gloabFunction getSign:@"shopRejectRefund" strParams:self.refundCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [self.jsonPrcClient invokeMethod:@"shopRejectRefund" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSString *code = [responseObject objectForKey:@"code"];
        
        if (code.intValue==50000) {
            NSLog(@"不同意成功");
            [self cancelBuClick:nil];
            
            page = 1;
            [self getRefundApplicationList];

            ////test
//            NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:[self.mdata objectAtIndex:self.btnForRow]];
//            [self.mdata removeObjectAtIndex:self.btnForRow];
//            [dic setObject:@"3" forKey:@"handleFlag"];
//            [self.mdata insertObject:dic atIndex:self.btnForRow];
//            
//            [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:[NSIndexPath indexPathForRow:self.btnForRow inSection:0], nil] withRowAnimation:UITableViewRowAnimationNone];
            
            
            
        }else if (code.intValue == 20000) {
            CSAlert(@"失败，请重试");
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

    }];

}


//avatarUrl	用户头像	string
//deduction	优惠金额	number	单位：元
//mobileNbr	用户手机号	number
//nickName	昵称	string
//orderAmount	消费金额	number	单位：元
//orderNbr	订单号	string
//payedTime	消费时间	string	格式：yyyy年mm月dd日hh:ii:ss
//realPay	实际支付金额	number	单位：元
//refundCode	退款编码	string
//refundReason	退款理由	string


- (void)getRefundApplicationList {
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:page] forKey:@"page"];
    
    NSString* vcode = [gloabFunction getSign:@"getRefundApplicationList" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [self.jsonPrcClient invokeMethod:@"getRefundApplicationList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [self.tableView headerEndRefreshing];
        [self.tableView footerEndRefreshing];
        
        if (page == 1) {
            [self.mdata removeAllObjects];

            [self.tableView setContentOffset:CGPointMake(0,0) animated:NO];
        }
        
        
        NSArray *array = [responseObject objectForKey:@"list"];
        
        [self.mdata addObjectsFromArray:array];
        [self.tableView reloadData];
        
        if ([[responseObject objectForKey:@"totalCount"] intValue] ==  self.mdata.count) {
            [self.tableView removeFooter];
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        [self.tableView headerEndRefreshing];
        [self.tableView footerEndRefreshing];
    }];

}

- (void)getShopCardList {
    
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:0] forKey:@"page"];
    
    NSString* vcode = [gloabFunction getSign:@"getShopCardList" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [self.jsonPrcClient invokeMethod:@"getShopCardList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if (self.backCardArray) {
            [self.backCardArray removeAllObjects];
        }
        
        
        [self.backCardArray addObjectsFromArray:[responseObject objectForKey:@"list"]];
        if (self.backCardArray.count > 0) {
            self.backCardDic = [self.backCardArray objectAtIndex:0];
            
        }

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

    }];

    
}

#pragma mark - 获取银行短信验证码
- (void)getIcbcMsgValCode {
    [SVProgressHUD showWithStatus:@"" maskType:SVProgressHUDMaskTypeClear];
    if ([self.backCardDic count]==0) {
        CSAlert(@"银行卡为空");
        return;
    }
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString *backAccountCode = [NSString stringWithFormat:@"%@", [self.backCardDic objectForKey:@"bankAccountCode"]];
    [params setObject:backAccountCode forKey:@"bankAccountCode"];
    [params setObject:self.refundCode forKey:@"refundCode"];
    NSString* vcode = [gloabFunction getSign:@"getIcbcMsgValCode" strParams:backAccountCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [self.jsonPrcClient invokeMethod:@"getIcbcMsgValCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        switch (code.intValue) {
            case 50000:{
                DCPaymentView *payView = [self.view viewWithTag:9000];
                [payView timerFireMethod];
                
            }
                break;
            case 20000:
                CSAlert(@"获取失败");
                break;
                
            default:
                break;
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
}


//是否设置了支付密码
- (void)httpIfShopSetPayPwd {
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"ifShopSetPayPwd" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [self.jsonPrcClient invokeMethod:@"ifShopSetPayPwd" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        self.ifShopSetPayPwd = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
}



@end
