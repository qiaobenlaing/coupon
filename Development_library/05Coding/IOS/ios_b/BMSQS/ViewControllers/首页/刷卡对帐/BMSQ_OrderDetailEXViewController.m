//
//  BMSQ_OrderDetailEXViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_OrderDetailEXViewController.h"
#import "UIImageView+WebCache.h"
#import "UIButtonEx.h"
#import "SVProgressHUD.h"
#import "BMSQ_MemberChartViewController.h"

//#define NODiscountPrice 6
#define ORDERTIME 4  //下单时间
#define ORDERPREFE 6 //优惠


@interface BMSQ_OrderDetailEXViewController ()
{
    
//    UITableView *m_tableView;
    
    UILabel *lb_orderPay;
    UILabel *lb_userName;
    UILabel *lb_orderNbr;
    UILabel *lb_phone;
    UILabel *lb_orderType;
    UILabel *lb_orderAmount;
    UILabel *lb_deduction;
    UILabel *lb_realPay;
    UILabel *lb_payType;
    UILabel *lb_noDiscountPrice;
    UILabel *lb_consumeTime;
    
    UILabel *lb_platBonus; //平台红包 label
    UILabel *lb_shopBonus; //商家红包 label
    
    UILabel *lb_batchNbrFunction; //批次 说明
    UILabel *lb_couponCode ;  //兑换码
    
    
    NSMutableDictionary *m_dic;
    
    BOOL isSelect;
    
    int preferentialNum;
    
    CGFloat subViewY;
    
    
}
@property (nonatomic,strong) UIView *subView;
@property (nonatomic, strong)UIView *view9;
@property (nonatomic, strong)UIScrollView *scrollView;


@end

@implementation BMSQ_OrderDetailEXViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"银行卡对账"];
    [self setNavBackItem];
    
    [self getOrderDetails];
    
    [self setViewUp];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setViewUp {
 
    

//    lb_orderPay = [[UILabel alloc] initWithFrame:CGRectMake(0, 44, APP_VIEW_WIDTH, 44)];
//    lb_orderPay.textAlignment = NSTextAlignmentCenter;
//    lb_orderPay.backgroundColor = [UIColor clearColor];
//    lb_orderPay.textColor = [UIColor redColor];
//    lb_orderPay.font = [UIFont systemFontOfSize:20];
//    lb_orderPay.text = @"";
    
    
//    lb_userName = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3*2, 70)];
//    lb_userName.backgroundColor = [UIColor clearColor];
//    lb_userName.font = [UIFont systemFontOfSize:13];
//    lb_userName.text = @"";
    
//    lb_phone = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3*2, 44)];
//    lb_phone.backgroundColor = [UIColor clearColor];
//    lb_phone.font = [UIFont systemFontOfSize:13];
//    lb_phone.text = @"";
    
    
//    lb_orderNbr = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3*2, 44)];
//    lb_orderNbr.backgroundColor = [UIColor clearColor];
//    lb_orderNbr.font = [UIFont systemFontOfSize:13];
//    lb_orderNbr.text = @"";
    
//    lb_payType = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3*2, 44)];
//    lb_payType.backgroundColor = [UIColor clearColor];
//    lb_payType.font = [UIFont systemFontOfSize:13];
//    lb_payType.text = @"";
    
    
//    lb_orderType = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44)];
//    lb_orderType.backgroundColor = [UIColor clearColor];
//    lb_orderType.font = [UIFont systemFontOfSize:15];
//    lb_orderType.text = @"";
    
//    lb_orderAmount = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2-20, 44)];
//    lb_orderAmount.backgroundColor = [UIColor clearColor];
//    lb_orderAmount.textAlignment = NSTextAlignmentRight;
//    lb_orderAmount.font = [UIFont systemFontOfSize:13];
//    lb_orderAmount.text = @"0.00";
//    
//    lb_deduction = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2-20, 44)];
//    lb_deduction.backgroundColor = [UIColor clearColor];
//    lb_deduction.textAlignment = NSTextAlignmentRight;
//    lb_deduction.font = [UIFont systemFontOfSize:13];
//    lb_deduction.text = @"0.00";
//    
//    
//    lb_noDiscountPrice = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 44, APP_VIEW_WIDTH/2-20, 44)];
//    lb_noDiscountPrice.backgroundColor = [UIColor clearColor];
//    lb_noDiscountPrice.textAlignment = NSTextAlignmentRight;
//    lb_noDiscountPrice.font = [UIFont systemFontOfSize:13];
//    lb_noDiscountPrice.text = @"";
//    
//    
//    lb_realPay= [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2-20, 44)];
//    lb_realPay.backgroundColor = [UIColor clearColor];
//    lb_realPay.textAlignment = NSTextAlignmentRight;
//    lb_realPay.textColor = [UIColor redColor];
//    lb_realPay.font = [UIFont systemFontOfSize:13];
//    lb_realPay.text = @"0.00";
    
    
    [self setViewUp2];

}

- (void)setViewUp2 {
    
    isSelect = NO;
    
    self.scrollView  = [[UIScrollView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y)];
    self.scrollView.delegate = self;
    [self.view addSubview:self.scrollView];
    
    //Y的高度
    CGFloat viewY;
    viewY = 5;
    UIView *view0 = [self drawView:viewY height:90];
  
    lb_orderType = [self drawLabel:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44) alignment:0 size:15.f];
    lb_orderPay = [self drawLabel:CGRectMake(0, 44, APP_VIEW_WIDTH, 44) alignment:2 size:20.f];
    [view0 addSubview:lb_orderType];
    [view0 addSubview:lb_orderPay];
    
    
    //头像昵称
    viewY = viewY + view0.frame.size.height + 10;
    UIView *view1 = [self drawView:viewY height:70];

    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = view1.bounds;
    [view1 addSubview:button];
    button.backgroundColor = [UIColor clearColor];
    [button setImage:[UIImage imageNamed:@"garyright"] forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    button.imageEdgeInsets = UIEdgeInsetsMake(20, APP_VIEW_WIDTH-40, 20, 10);
    
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 50, 50)];
    imageView.backgroundColor = [UIColor clearColor];
    NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[m_dic objectForKey:@"avatarUrl"]];
    [imageView sd_setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    [view1 addSubview:imageView];
    
    lb_userName = [self drawLabel:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3*2, 70) alignment:0 size:13.f];
    [view1 addSubview:lb_userName];
    

    //手机号码
    viewY = viewY + view1.frame.size.height;
    UIView *view2 = [self drawView:viewY height:44];
    [self drawleftLabel:@"手机号码" forView:view2];
    lb_phone = [self drawCenterLabel:view2];
    
    
    //订单号
    viewY = viewY + view2.frame.size.height+10;
    UIView *view3 = [self drawView:viewY height:44];
    [self drawleftLabel:@"订单号" forView:view3];
    lb_orderNbr = [self drawCenterLabel:view3];
    
    
    if (self.coupon78) {
        //批次
        viewY = viewY + 44;
        UIView *couponView1 = [self drawView:viewY height:44];
        [self drawleftLabel:@"批次" forView:couponView1];
        lb_batchNbrFunction = [self drawCenterLabel:couponView1];
        
        //兑换码
        viewY = viewY + 44;
        UIView *couponView2 = [self drawView:viewY height:44];
        [self drawleftLabel:@"兑换码" forView:couponView2];
        lb_couponCode = [self drawCenterLabel:couponView2];
        
        
    }else {
        
        //支付类型
        viewY = viewY + 44;
        UIView *view4 = [self drawView:viewY height:44];
        [self drawleftLabel:@"支付类型" forView:view4];
        lb_payType = [self drawCenterLabel:view4];
        
        
        
    }
    
    
    
    
    
    //下单时间
    viewY = viewY + 44;
    UIView *view5 = [self drawView:viewY height:44];
    [self drawleftLabel:@"下单时间" forView:view5];
    lb_consumeTime = [self drawCenterLabel:view5];
    
    
    //消费金额
    viewY = viewY + view5.frame.size.height + 10;
    UIView *view6 = [self drawView:viewY height:44];
    [self drawleftLabel:@"消费金额" forView:view6];
    lb_orderAmount = [self drawRightLabel:view6];
    
    
    if (self.coupon78) {
        //商家红包
        viewY = viewY + 44;
        UIView *couponView1 = [self drawView:viewY height:44];
        [self drawleftLabel:@"商家红包" forView:couponView1];
        lb_shopBonus = [self drawRightLabel:couponView1];
        
        //惠圈红包
        viewY = viewY + 44;
        UIView *couponView2 = [self drawView:viewY height:44];
        [self drawleftLabel:@"惠圈红包" forView:couponView2];
        lb_platBonus = [self drawRightLabel:couponView2];
        

    }else {

        //不参与优惠金额
        viewY = viewY + 44;
        UIView *view7 = [self drawView:viewY height:44];
        [self drawleftLabel:@"不参与优惠金额" forView:view7];
        lb_noDiscountPrice = [self drawRightLabel:view7];
        
        
        //优惠金额
        viewY = viewY + 44;
        UIView *view8 = [self drawView:viewY height:44];
        [self drawleftLabel:@"优惠金额" forView:view8];
        lb_deduction = [self drawRightLabel:view8];
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        button.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, 44);
        [button addTarget:self action:@selector(clickDeductionBtn:) forControlEvents:UIControlEventTouchUpInside];
        [view8 addSubview:button];
        button.imageEdgeInsets = UIEdgeInsetsMake(20, APP_VIEW_WIDTH-20, 20, 10);
        [button setImage:[UIImage imageNamed:@"iv_arrowBottomHeight"] forState:UIControlStateNormal];
        [button setImage:[UIImage imageNamed:@"iv_arrowTopHeight"] forState:UIControlStateSelected];
        //iv_arrow.image = [UIImage imageNamed:@"iv_arrowBottomHeight"];
        //[view addSubview:iv_arrow];
        
        
        
        subViewY = viewY + 44;
        
        
    }
    
    //    if (isSelect) {
    //        viewY = viewY + 25*5;}
    
    
    
    //实际支付
    viewY = viewY + 44;
    
    self.view9 = [self drawView:viewY height:44];
    [self drawleftLabel:@"实际支付" forView:self.view9];
    lb_realPay = [self drawRightLabel:self.view9];
    
    
    self.scrollView.contentSize = CGSizeMake(APP_VIEW_WIDTH, viewY+44);
    


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
            lb_userName.text = [m_dic objectForKey:@"nickName"];
            lb_orderNbr.text = [m_dic objectForKey:@"orderNbr"];
            lb_orderAmount.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"orderAmount"]];
            lb_deduction.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"deduction"]];
            lb_realPay.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"realPay"]];
            lb_phone.text = [NSString stringWithFormat:@"%@", [m_dic objectForKey:@"userMobileNbr"]];
            
            if (self.coupon78) {
                lb_batchNbrFunction.text = [NSString stringWithFormat:@"%@      %@", [m_dic objectForKey:@"batchNbr"], [m_dic objectForKey:@"function"]];
                lb_couponCode.text = [NSString stringWithFormat:@"%@", [m_dic objectForKey:@"couponCode"]];
                lb_shopBonus.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"shopBonusDeduction"]];
                lb_platBonus.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"platBonusDeduction"]];
                
            }
            
            
            NSNumber *noDisountPriceNm = [m_dic objectForKey:@"noDiscountPrice"];
           
             lb_noDiscountPrice.text = [NSString stringWithFormat:@"%@元", [m_dic objectForKey:@"noDiscountPrice"]];
            if ([noDisountPriceNm isEqual:@""]){
                 lb_noDiscountPrice.text = [NSString stringWithFormat:@"0.00元"];
            };
            
            NSArray *orderTypeArray = [NSArray arrayWithObjects:@"", @"未付款", @"付款中", @"已付款", @"已取消付款", @"付款失败", @"退款申请中", @"已退款", nil];
            int num = [[m_dic objectForKey:@"status"] intValue];
            if (num <= orderTypeArray.count) {
                lb_orderType.text = orderTypeArray[num];
                
            }
            
            lb_consumeTime.text = [m_dic objectForKey:@"consumeTime"];
            
            switch (num) {
                case 3:
                    lb_orderPay.text = [NSString stringWithFormat:@"%@元", [m_dic objectForKey:@"realPay"]];
                    break;
                    
                case 6:
                    lb_orderPay.text = [NSString stringWithFormat:@"%@元", [m_dic objectForKey:@"realPay"]];
                    break;
                    
                case 7:
                    lb_orderPay.text = [NSString stringWithFormat:@"-%@元", [m_dic objectForKey:@"realPay"]];
                    break;
                    
                default:
                    break;
            }
            
            if ([[m_dic objectForKey:@"firstDeduction"] intValue] > 0.00) {
                preferentialNum = 6;
                
            }else  {
                preferentialNum = 5;
                
            }
            
            
            int payTypeInt = [[m_dic objectForKey:@"payType"] intValue];
            NSArray *payTypeArray = [NSArray arrayWithObjects:@"", @"工银在线支付", @"POS机支付", @"POS机支付", @"未选择支付方式", @"实物券和体验券的支付",  nil];
            if (payTypeInt < payTypeArray.count) {
                lb_payType.text = payTypeArray[payTypeInt];
                
            }
            
            
            self.subView = [self drawView:subViewY height:25*5];
            [self.scrollView addSubview:self.subView];
            
            
            for (int i = 0; i<5; i++) {
                UILabel *subleftLaebl = [[UILabel alloc] initWithFrame:CGRectMake(20, 25*i, APP_VIEW_WIDTH-(20*2), 20)];
                subleftLaebl.font = [UIFont systemFontOfSize:11];
                subleftLaebl.textColor = [UIColor darkGrayColor];
                [self.subView addSubview:subleftLaebl];
                
                UILabel *subRightLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, 25*i, APP_VIEW_WIDTH-(20*2), 20)];
                subRightLabel.textAlignment = NSTextAlignmentRight;
                subRightLabel.backgroundColor = [UIColor clearColor];
                subRightLabel.font = [UIFont systemFontOfSize:11];
                subRightLabel.textColor = [UIColor darkGrayColor];
                [self.subView addSubview:subRightLabel];
                
                
                switch (i) {
                    case 0:
                        subleftLaebl.text = [self getCouponType];
                        subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"couponDeduction"]];
                        break;
                    case 1:{
                        subleftLaebl.text = @"会员卡 : ";
                        if (m_dic) {
                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"cardDeduction"]];
                        }
                        break;
                    }
                    case 2:{
                        subleftLaebl.text = @"工银优惠 : ";
                        if (m_dic) {
                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"bankCardDeduction"]];
                        }
                    }
                        break;
                    case 3:{
                        subleftLaebl.text = @"商家红包 :";
                        if (m_dic) {
                            
                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"shopBonusDeduction"]];
                        }
                    }
                        break;
                    case 4:{
                        subleftLaebl.text = @"惠圈红包 :";
                        if (m_dic) {
                            
                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"platBonusDeduction"]];
                        }
                    }
                        break;
                    case 5:{
                        subleftLaebl.text = @"首单立减 :";
                        if (m_dic) {
                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"firstDeduction"]];
                        }
                    }
                        break;
                        
                    default:
                        break;
                }
                self.subView.hidden = YES;
                
            }
            
            
            
        }
        
//        [m_tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        
    }];
    
}

- (NSString *)getCouponType{
    NSString *couponTypeNtr;
    if ([[m_dic objectForKey:@"couponType"]  isEqual: @""]) {
        couponTypeNtr = @"优惠券 :";
        
    }else {
        int couponType = [[m_dic objectForKey:@"couponType"] intValue];
        
        switch (couponType) {
            case 3:
                couponTypeNtr = [NSString stringWithFormat:@"抵扣券:满%@减%@,使用%@张",[m_dic objectForKey:@"availablePrice"], [m_dic objectForKey:@"insteadPrice"], [m_dic objectForKey:@"couponUsed"]];
                break;
                
            case 5:
                couponTypeNtr = [NSString stringWithFormat:@"折扣券:满%@打%@折 ",[m_dic objectForKey:@"availablePrice"], [m_dic objectForKey:@"discountPercent"]];
                break;
                
                
            default:
                couponTypeNtr = @"优惠券";
                
                
                break;
        }
        
        
    }
    
    
    return couponTypeNtr;
}


- (UILabel *)drawCenterLabel:(UIView *)view{
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3*2, 44)];
    label.font = [UIFont systemFontOfSize:13.f];
    [view addSubview:label];
    
    return label;
}

- (UILabel *)drawRightLabel:(UIView *)view{
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3*2-20, 44)];
    label.font = [UIFont systemFontOfSize:13.f];
    [view addSubview:label];
    label.textAlignment = NSTextAlignmentRight;
    return label;
}


- (UILabel *)drawleftLabel:(NSString *)title  forView:(UIView *)view{
    UILabel *label = [self drawLabel:CGRectMake(10, 0, APP_VIEW_WIDTH/2, 44) alignment:0 size:13.f];
    label.text = title;
    [view addSubview:label];
    
    return label;
}




- (UIView *)drawView:(CGFloat)viewY height:(CGFloat)viewHeight {
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, viewY, APP_VIEW_WIDTH, viewHeight)];
    view.backgroundColor = [UIColor whiteColor];
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, viewHeight-0.5, APP_VIEW_WIDTH, 0.5)];
    line.backgroundColor = APP_VIEW_BACKCOLOR;
    [view addSubview:line];
    
    [self.scrollView addSubview:view];
    
    return view;
    
}


- (UILabel *)drawLabel:(CGRect)frame alignment:(int)alignment size:(CGFloat)size{
    UILabel *label = [[UILabel alloc] initWithFrame:frame];
    label.backgroundColor = [UIColor clearColor];
    
    
    switch (alignment) {
        case 0:
            label.textAlignment = NSTextAlignmentLeft;
            break;
        case 1:{
            label.textAlignment = NSTextAlignmentRight;
        }
            break;
        case 2:{
            label.textAlignment = NSTextAlignmentCenter;
        }
            break;
        default:
            break;
    }
    
    label.font = [UIFont systemFontOfSize:size];
    
    return label;
}


- (void)clickDeductionBtn:(UIButton *)button {
    
    isSelect = !isSelect;
    if (isSelect) {
        button.selected = YES;
        self.scrollView.contentSize = CGSizeMake(APP_VIEW_WIDTH, self.scrollView.contentSize.height + 25*5);
        self.subView.hidden = NO;
        self.view9.frame = CGRectMake(self.view9.frame.origin.x, self.view9.frame.origin.y + 25*5, self.view9.frame.size.width, self.view9.frame.size.height);
        
    } else {
        button.selected = NO;
        self.scrollView.contentSize = CGSizeMake(APP_VIEW_WIDTH, self.scrollView.contentSize.height - 25*5);
        self.subView.hidden = YES;
        self.view9.frame = CGRectMake(self.view9.frame.origin.x, self.view9.frame.origin.y - 25*5, self.view9.frame.size.width, self.view9.frame.size.height);
        
    }
    


}


- (void)btnAction:(UIButton *)btn {
    
    BMSQ_MemberChartViewController *vc = [[BMSQ_MemberChartViewController alloc] init];
    vc.userID = [m_dic objectForKey:@"userCode"];
    //        vc.hidesBottomBarWhenPushed = YES;
    
    vc.titleNav = [NSString stringWithFormat:@"给%@留言",[m_dic objectForKey:@"nickName"]];
    [self.navigationController pushViewController:vc animated:YES];
    
}




#pragma mark - UITableView delegate

//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
//    if (indexPath.section == 1) {
//        if (indexPath.row == 0){
//            return 70;
//        }else if (indexPath.row == 1 || indexPath.row == ORDERTIME) {
//            return 54;
//        }else if (indexPath.row == ORDERPREFE) {
//            if (!isSelect) {
//                return 44;
//            }else{
//                return 44+25*preferentialNum;
//            }
//        }else if (indexPath.row == 5) {
////            if (lb_noDiscountPrice.text.length == 0) {
////
////            }else {
//                return 88;
//
////            }
//        }
//
//        return 44;
//    }
//    ;
//}
//
//- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
//
//    return 2;
//}
//
//- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
//    if (section == 0) {
//        return 1;
//    }
//    return 8;
//}
//
//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
//    if (indexPath.section == 0) {
//        UITableViewCell *cell = [[UITableViewCell alloc] init];
//        cell.backgroundColor = APP_VIEW_BACKCOLOR;
//        cell.selectionStyle = UITableViewCellSelectionStyleNone;
//
//        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 90)];
//        view.backgroundColor = [UIColor whiteColor];
//        [cell.contentView addSubview:view];
//
//        [view addSubview:lb_orderType];
//        [view addSubview:lb_orderPay];
//
//        return cell;
//
//    }
//    if (indexPath.section == 1) {
//
//        UITableViewCell *cell = [[UITableViewCell alloc] init];
//        cell.backgroundColor = [UIColor whiteColor];
//        cell.selectionStyle = UITableViewCellSelectionStyleNone;
//
//        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 44)];
//        view.backgroundColor = [UIColor whiteColor];
//        [cell.contentView addSubview:view];
//
//        UILabel *lb_left = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-40, 44)];
//        lb_left.backgroundColor = [UIColor clearColor];
//        lb_left.font = [UIFont systemFontOfSize:13];
//        [view addSubview:lb_left];
//
//
//        if (indexPath.row == 0) {
//
//            lb_left.hidden = YES;
//            view.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, 70);
//
//            UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 50, 50)];
//            [view addSubview:imageView];
//            imageView.backgroundColor = [UIColor clearColor];
//            NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[m_dic objectForKey:@"avatarUrl"]];
//            [imageView sd_setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
//
//            [view addSubview:lb_userName];
//
//            UIImageView *rightImageView = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-30, (70-15)/2, 10, 15)];
//            //    imageView.backgroundColor = [UIColor greenColor];
//            [rightImageView setImage:[UIImage imageNamed:@"iv_Right"]];
//            [view addSubview:rightImageView];
//
//
//            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 69.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
//            line.backgroundColor = APP_CELL_LINE_COLOR;
//            [view  addSubview:line];
//
//
//        }else if (indexPath.row == 1) {
//
//
//            lb_left.text = @"手机号码";
//            [view addSubview:lb_phone];
//
//            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 44, APP_VIEW_WIDTH, 10)];
//            line.backgroundColor = APP_VIEW_BACKCOLOR;
//            [view addSubview:line];
//
//        }else if (indexPath.row == 2) {
//
//            lb_left.text = @"订单号";
//            [view addSubview:lb_orderNbr];
//
//            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
//            line.backgroundColor = APP_CELL_LINE_COLOR;
//            [view addSubview:line];
//
//
//        }else if (indexPath.row == 3) {
//
//            lb_left.text = @"支付类型";
//
//            [cell addSubview:lb_payType];
//
//            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
//            line.backgroundColor = APP_CELL_LINE_COLOR;
//            [view addSubview:line];
//
//
//        }else if (indexPath.row == ORDERTIME) {//下单时间
//
//            lb_left.text = @"下单时间:";
//
//            UILabel *lb_consumeTime = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3*2, 44)];
//            lb_consumeTime.font = [UIFont systemFontOfSize:13];
//            if (m_dic) {
//                lb_consumeTime.text = [m_dic objectForKey:@"consumeTime"];
//            }
//
//            [view  addSubview:lb_consumeTime];
//
//            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 44, APP_VIEW_WIDTH, 10)];
//            line.backgroundColor = APP_VIEW_BACKCOLOR;
//            [view addSubview:line];
//
//            return cell;
//
//        }else if (indexPath.row == 5) {
//
////            if (lb_noDiscountPrice.text.length == 0) {
////                lb_left.text = @"消费金额";
////
////                [view addSubview:lb_orderAmount];
////
////                UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
////                line.backgroundColor = APP_CELL_LINE_COLOR;
////                [view addSubview:line];
////            }else {
//            view.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, 44*2);
//            lb_left.text = @"消费金额";
//
//            [view addSubview:lb_orderAmount];
//
//            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
//            line.backgroundColor = APP_CELL_LINE_COLOR;
//            [view addSubview:line];
//
//
//            UILabel *leftLabel2 = [[UILabel alloc] initWithFrame:CGRectMake(10, 44, APP_VIEW_WIDTH/2, 44)];
//            leftLabel2.font =[ UIFont systemFontOfSize:13.f];
//            leftLabel2.text = @"不参与优惠金额";
//            [view addSubview:leftLabel2];
//            [view addSubview:lb_noDiscountPrice];
//
//            UIView *line2 = [[UIView alloc] initWithFrame:CGRectMake(0, 87.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
//            line2.backgroundColor = APP_CELL_LINE_COLOR;
//            [view addSubview:line2];
//
////            }
//
//
//
//
//
//        }else if (indexPath.row == ORDERPREFE) {
//            if (!isSelect) {
//
//                lb_left.text = @"优惠金额";
//
//                [view addSubview:lb_deduction];
//
//                UIImageView *iv_arrow = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-20, 19, 10, 7)];
//                iv_arrow.backgroundColor = [UIColor clearColor];
//                iv_arrow.image = [UIImage imageNamed:@"iv_arrowBottomHeight"];
//                [view addSubview:iv_arrow];
//
//                UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
//                line.backgroundColor = APP_CELL_LINE_COLOR;
//                [view addSubview:line];
//
//
//
//            }else {
//
//                lb_left.text = @"优惠金额";
//                [view addSubview:lb_deduction];
//
//                UIImageView *iv_arrow = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-20, 19, 10, 7)];
//                iv_arrow.backgroundColor = [UIColor clearColor];
//                iv_arrow.image = [UIImage imageNamed:@"iv_arrowTopHeight"];
//                [view addSubview:iv_arrow];
//
//                UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
//                line.backgroundColor = APP_CELL_LINE_COLOR;
//                [view addSubview:line];
//
//
//
//
//                for (int i=0; i<preferentialNum; i++) {
//                    UILabel *subleftLaebl = [[UILabel alloc] initWithFrame:CGRectMake(20, 44+25*i, APP_VIEW_WIDTH-(20*2), 20)];
//                    subleftLaebl.font = [UIFont systemFontOfSize:11];
//                    subleftLaebl.textColor = [UIColor darkGrayColor];
//                    [cell addSubview:subleftLaebl];
//
//                    UILabel *subRightLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, 44+25*i, APP_VIEW_WIDTH-(20*2), 20)];
//                    subRightLabel.textAlignment = NSTextAlignmentRight;
//                    subRightLabel.backgroundColor = [UIColor clearColor];
//                    subRightLabel.font = [UIFont systemFontOfSize:11];
//                    subRightLabel.textColor = [UIColor darkGrayColor];
//                    [view addSubview:subRightLabel];
//
//
//                    if (i == 0) {
//
//                        if (m_dic) {
//
//                            subleftLaebl.text = [self getCouponType];
//                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"couponDeduction"]];
//
//
//                        }
//
//                    }
//
//                    if (i == 1) {
//                        subleftLaebl.text = @"会员卡 : ";
//                        if (m_dic) {
//                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"cardDeduction"]];
//                        }
//
//                    }else if (i == 2) {
//
//                        subleftLaebl.text = @"工银优惠 : ";
//                        if (m_dic) {
//                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"bankCardDeduction"]];
//                        }
//
//
//
//                    }else if (i == 3) {
//                        subleftLaebl.text = @"商家红包 :";
//                        if (m_dic) {
//                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"shopBonusDeduction"]];
//                        }
//                    }else if (i == 4) {
//                        subleftLaebl.text = @"惠圈红包 :";
//                        if (m_dic) {
//                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"platBonusDeduction"]];
//                        }
//
//                    }else if (i == 5) {
//                        subleftLaebl.text = @"首单立减 :";
//                        if (m_dic) {
//                            subRightLabel.text = [NSString stringWithFormat:@"%@元",[m_dic objectForKey:@"firstDeduction"]];
//                        }
//
//                    }
//
//
//                }
//
//                UIView *line1 = [[UIView alloc] initWithFrame:CGRectMake(0, 44+25*preferentialNum-0.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
//                line1.backgroundColor = APP_CELL_LINE_COLOR;
//                [view addSubview:line1];
//
//
//            }
//
//        }else if (indexPath.row == 7) {
//
//
//            lb_left.text = @"实际支付";
//
//            [cell addSubview:lb_realPay];
//
//            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
//            line.backgroundColor = APP_CELL_LINE_COLOR;
//            [cell addSubview:line];
//
//
//        }
//        return cell;
//    }
//
//
//    return nil;
//}
//
//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
//
//
//    if (indexPath.section==1 && indexPath.row==0) {
//        BMSQ_MemberChartViewController *vc = [[BMSQ_MemberChartViewController alloc] init];
//        vc.userID = [m_dic objectForKey:@"userCode"];
////        vc.hidesBottomBarWhenPushed = YES;
//
//        vc.titleNav = [NSString stringWithFormat:@"给%@留言",[m_dic objectForKey:@"nickName"]];
//        [self.navigationController pushViewController:vc animated:YES];
//
//    }
//
//    if (indexPath.row == ORDERPREFE) {
//        isSelect = !isSelect;
//
//        [m_tableView reloadRowsAtIndexPaths:@[indexPath]
//                           withRowAnimation:UITableViewRowAnimationFade];
//    }
//
//
//}


@end
