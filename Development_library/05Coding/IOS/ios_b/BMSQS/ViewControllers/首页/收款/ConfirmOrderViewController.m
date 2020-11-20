//
//  ConfirmOrderViewController.m
//  BMSQS
//
//  Created by gh on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ConfirmOrderViewController.h"
#import "OrderFinishViewController.h"
#import "UIImageView+WebCache.h"
#import "SVProgressHUD.h"

@interface ConfirmOrderViewController () {
    UIAlertController *alertC;
}

@property (nonatomic, strong)UIScrollView *scrollView;




@end


@implementation ConfirmOrderViewController


- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    // 禁用 iOS 返回手势
    if ([self.navigationController respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
        self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    }
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    // 开启
    if ([self.navigationController respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
        self.navigationController.interactivePopGestureRecognizer.enabled = YES;
    }
}



- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"确认订单"];
    [self setviewUp];
    
}

- (void)setNavBackItem
{
 
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT ), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    [btnback addTarget:self action:@selector(cancelBankcardPay) forControlEvents:UIControlEventTouchUpInside];
    [self.navigationView addSubview:btnback];
}




- (void)setviewUp {
    
    [self setAlretController];
    
    self.view.backgroundColor = UICOLOR(247, 247, 247, 1);
    self.scrollView  = [[UIScrollView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y)];
    self.scrollView.backgroundColor = [UIColor clearColor];
    // 是否支持滑动最顶端
    //    scrollView.scrollsToTop = NO;
    self.scrollView.delegate = self;
    [self.view addSubview:self.scrollView];
    
    NSString *left = @"left";
    NSString *right = @"right";
    CGFloat viewY = 0;
    CGFloat height = 100;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, viewY, APP_VIEW_WIDTH, height)];
    view.backgroundColor = UICOLOR(255, 255, 255, 1);
    [self.scrollView addSubview:view];
    
    
    NSDictionary *userInfoDic = [self.orderData objectForKey:@"userInfo"];
    NSString *nickName = [userInfoDic objectForKey:@"nickName"];
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH-50)/2, 25, 50, 50)];
    [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, [userInfoDic objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    imageView.backgroundColor = [UIColor blueColor];
    [view addSubview:imageView];
    
    UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake( 0, imageView.frame.size.height + imageView.frame.origin.y, APP_VIEW_WIDTH, 20)];
    nameLabel.text = [NSString stringWithFormat:@"%@", nickName];
    nameLabel.textAlignment = NSTextAlignmentCenter;
    [view addSubview: nameLabel];
    
    //消费金额
    viewY = viewY + height + 10;
    UIView *view1 = [self drawView:viewY];
    [self drawLabel:left Parentview:(UIView *)view1 title:@"消费金额"];
    [self drawLabel:right Parentview:(UIView *)view1  title:[NSString stringWithFormat:@"%@元",self.orderAmount]];

  
    //不参与优惠金额
    viewY = viewY + 44 + 10;
    UIView *view2 = [self drawView:viewY];
    [self drawLabel:left Parentview:(UIView *)view2 title:@"不参与优惠金额"];
    if ([self.noDiscountPrice isEqual:@""]){
        self.noDiscountPrice = @"0";
    }
    [self drawLabel:right Parentview:(UIView *)view2 title:[NSString stringWithFormat:@"%@元",self.noDiscountPrice]];


    
    
    
    //优惠券
    NSDictionary *couponDic = [self.orderData objectForKey:@"coupon"];
    NSString *couponType = [couponDic objectForKey:@"couponType"];
    if ([couponType isEqualToString:@""]) {
        
    }else {
    
        viewY = viewY + 44 +10;
        UIView *view3 = [self drawView:viewY];
        
        NSString *useNbr = [NSString stringWithFormat:@"%@", [couponDic objectForKey:@"useNbr"]];
        NSString *couponString = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"couponString"]];
        NSString *couponInsteadPrice = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"couponInsteadPrice"]];
        switch (couponType.intValue) {
            case 3:{ // 抵扣券
                [self drawLabel:left Parentview:(UIView *)view3 title:@"抵扣券"];
                [self drawLabel:right Parentview:(UIView *)view3 title:[NSString stringWithFormat:@"%@ X%@张 减免%@元", couponString, useNbr, couponInsteadPrice]];
            }
                break;
            case 4:{ //折扣券
                [self drawLabel:left Parentview:(UIView *)view3 title:@"折扣券"];
                [self drawLabel:right Parentview:(UIView *)view3 title:[NSString stringWithFormat:@"%@  减免%@元", couponString, couponInsteadPrice]];
            }
                break;
            case 7:{ //兑换券
                [self drawLabel:left Parentview:(UIView *)view3 title:@"兑换券"];
                [self drawLabel:right Parentview:(UIView *)view3 title:[NSString stringWithFormat:@"%@", couponString]];
            }
                break;
            case 8:{ //代金券
                [self drawLabel:left Parentview:(UIView *)view3 title:@"代金券"];
                [self drawLabel:right Parentview:(UIView *)view3 title:[NSString stringWithFormat:@"%@", couponString]];
            }
                break;
            default:
                [self drawLabel:left Parentview:(UIView *)view3 title:@"优惠券"];
                break;
        }
    }
    
    //会员卡
    NSDictionary *cardDic = [self.orderData objectForKey:@"card"];
    NSString * cardInsteadPrice = [cardDic objectForKey:@"cardInsteadPrice"] ;
    NSString *cardDiscount =  [cardDic objectForKey:@"cardDiscount"];
    if (cardInsteadPrice.floatValue > 0.00) {
    
        viewY = viewY + 44 ;
        UIView *view4 = [self drawView:viewY];
        
        [self drawLabel:left Parentview:(UIView *)view4 title:[NSString stringWithFormat:@"会员卡打%@折", cardDiscount]];
        [self drawLabel:right Parentview:(UIView *)view4 title:[NSString stringWithFormat:@"%@元",cardInsteadPrice]];
    }

    
    //商家红包
    NSDictionary *bonusDic = [self.orderData objectForKey:@"bonus"];
    NSString *userShopBonus =[bonusDic objectForKey:@"userShopBonus"];
    NSString *shopBonus = [bonusDic objectForKey:@"shopBonus"];
    
    if (shopBonus.floatValue > 0.00) {
        viewY = viewY + 44 ;
        UIView *view5 = [self drawView:viewY];
        
        [self drawLabel:left Parentview:(UIView *)view5 title:[NSString stringWithFormat:@"商家红包%@元", shopBonus]];
        [self drawLabel:right Parentview:(UIView *)view5 title:[NSString stringWithFormat:@"商家红包可用%@元",userShopBonus ]];
    }
    
    
    
    NSString * userPlatBonus = [bonusDic objectForKey:@"userPlatBonus"];
    NSString * platBonus =  [bonusDic objectForKey:@"platBonus"];
    if (platBonus.floatValue > 0.00) {
        //平台红包  惠圈红包
        viewY = viewY + 44 ;
        UIView *view6 = [self drawView:viewY];
        
        [self drawLabel:left Parentview:(UIView *)view6 title:[NSString stringWithFormat:@"惠圈红包%@元",platBonus]];
        [self drawLabel:right Parentview:(UIView *)view6 title:[NSString stringWithFormat:@"惠圈红包可用%@元",userPlatBonus]];
        
    }
    
    
    

    //工行卡折扣
    NSDictionary *bankCardDic = [self.orderData objectForKey:@"bankCard"];
    NSString *bankCardDiscount = [bankCardDic objectForKey:@"bankCardDiscount"];
    NSString *bankCardDeduction = [bankCardDic objectForKey:@"bankCardDeduction"];
    if (bankCardDeduction.floatValue > 0.00) {
        viewY = viewY + 44 ;
        UIView *view7 = [self drawView:viewY];
        
        [self drawLabel:left Parentview:(UIView *)view7 title:[NSString stringWithFormat:@"工行卡折扣%@折", bankCardDiscount]];
        [self drawLabel:right Parentview:(UIView *)view7 title:[NSString stringWithFormat:@"工行卡可优惠%@元", bankCardDeduction]];
    }
    
    NSString *firstDeduction = [self.orderData objectForKey:@"firstDeduction"];
    if (firstDeduction.floatValue > 0.00) {
        viewY = viewY + 44;
        UIView *view8 = [self drawView:viewY];
        
        
        [self drawLabel:left Parentview:(UIView *)view8 title:@"首单立减"];
        [self drawLabel:right Parentview:(UIView *)view8 title:[NSString stringWithFormat:@"%@元", firstDeduction]];
    }
   
    
    
    
    //实际支付
    viewY = viewY + 44 + 10;
    UIView *view9 = [self drawView:viewY];
    NSString *realPay = [self.orderData objectForKey:@"realPay"];
    [self drawLabel:left Parentview:(UIView *)view9 title:@"实际支付"];
    [self drawLabel:right Parentview:(UIView *)view9 title:[NSString stringWithFormat:@"%@元", realPay]];

    
    
    //结算按钮
    viewY = viewY + 44 +10;
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    button.frame = CGRectMake(10, viewY, APP_VIEW_WIDTH - 20, 40);
    [button setTitle:@"结算" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnAction) forControlEvents:UIControlEventTouchUpInside];
    [self.scrollView addSubview:button];
    
    self.scrollView.contentSize = CGSizeMake(APP_VIEW_WIDTH, viewY+44 + 10);
}

- (void)setAlretController {
    alertC = [UIAlertController alertControllerWithTitle:@"你确定要去结算吗" message:nil preferredStyle:UIAlertControllerStyleAlert];
    [alertC addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        
        
    }]];
    [alertC addAction:[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        
        [self bankcardPayConfirm];

    }]];
    
    
    
}


- (void)btnAction {
    
    [self presentViewController:alertC animated:YES completion:^{
        
    }];

}


- (void)bankcardPayConfirm {
    
    [SVProgressHUD showWithStatus:@""];
    NSString *firstInt = [self.orderData objectForKey:@"firstDeduction"];
    if ([firstInt isEqual:@""] || firstInt.intValue == 0 || firstInt.intValue == 0.00) {
        firstInt = @"0";
    }else {
        firstInt = @"1";
    }
    
    
   
    [self initJsonPrcClient:@"1"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[_orderData objectForKey:@"consumeCode"] forKey:@"consumeCode"];
    [params setObject:self.bankAccountCode forKey:@"bankAccountCode"];
    [params setObject:firstInt forKey:@"isUseFirstDeduction"];
    [params setObject:@"1" forKey:@"payChanel"];
    
    NSString* vcode = [gloabFunction getSign:@"bankcardPayConfirm" strParams:[_orderData objectForKey:@"consumeCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"bankcardPayConfirm" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSLog(@"%@",responseObject);
        NSString *str = [responseObject objectForKey:@"code"];
        
//        50056, // 银行账户编码为空
//        50900, // 用户支付记录不存在
//        50057, // 银行账户编码错误
        if (str.intValue == 50000) {
            OrderFinishViewController *vc = [[OrderFinishViewController alloc] init];
            vc.dataDic = [responseObject objectForKey:@"consumeInfo"];
            [self.navigationController pushViewController:vc animated:YES];
            
        }else if (str.intValue == 20000) {
            CSAlert(@"请求错误");
        }else if (str.intValue == 50056){
            CSAlert(@"银行账户编码为空");
        }else if (str.intValue == 50900) {
            CSAlert(@"用户支付记录不存在");
        }else if (str.intValue == 50057) {
            CSAlert(@"银行账户编码错误");
        }else {
            NSString *retmsg = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"retmsg"]];
            if (retmsg) {
                CSAlert(retmsg);
            }
            
        }
        
        
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        NSLog(@"%@",error);
        
    }];
    
    
}

- (void)cancelBankcardPay {
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[_orderData objectForKey:@"consumeCode"] forKey:@"consumeCode"];
    
    NSString* vcode = [gloabFunction getSign:@"cancelBankcardPay" strParams:[_orderData objectForKey:@"consumeCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"cancelBankcardPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        50403, // 支付已经取消
//        20000, // 操作失败
//        50000, // 操作成功
        NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        switch (code.intValue) {
            case 50000:
                [self goBack];
                break;
            case 20000:
                break;
            case 50403:
                [self goBack];
                break;
            default:
                [self goBack];
                break;
        }
        
        
        
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        NSLog(@"%@",error);
        
    }];
    
    
    
}

- (void)goBack {
    
    [self.navigationController popViewControllerAnimated:YES];
    
    
}



- (UILabel *)drawLabel:(NSString *)rect Parentview:(UIView *)view title:(NSString *)titleStr{
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44)];
    label.backgroundColor = [UIColor clearColor];
    label.font = [UIFont systemFontOfSize:13.f];
    label.text = titleStr;
    
    if ([rect isEqualToString:@"left"]) {
        label.textAlignment = NSTextAlignmentLeft;
    }else  {
        label.textAlignment = NSTextAlignmentRight;
    }
    [view addSubview:label];
    return label;
    
}

- (UIView *)drawView:(CGFloat)frameY {
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, frameY, APP_VIEW_WIDTH, 44)];
    view.backgroundColor = UICOLOR(255, 255, 255, 1);
    [self.scrollView addSubview:view];
    return view;
    
}


@end
