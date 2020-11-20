//
//  BMSQ_PayDetailViewController.m
//  BMSQC
//
//  Created by liuqin on 15/8/28.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_PayDetailViewController.h"
#import "BMSQ_CouponQRcodeControllerViewController.h"
#import "BMSQ_QR.h"
#import "MobClick.h"
#import "BMSQ_PayCardViewController.h"
#import "SVProgressHUD.h"
@interface BMSQ_PayDetailViewController ()


@property (nonatomic, strong)NSDictionary *couponDic;

@end

@implementation BMSQ_PayDetailViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"PayDetail"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"PayDetail"];
}


- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"支付详情"];
    
    [self getUserCouponInfo];

 
}

#pragma mark - 数据请求
- (void)getUserCouponInfo
{
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.userCouponCode forKey:@"userCouponCode"];
    NSString* vcode = [gloabFunction getSign:@"getUserCouponInfo" strParams:self.userCouponCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getUserCouponInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        weakSelf.couponDic = (NSDictionary *)responseObject;
        [weakSelf setMyView:responseObject];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
}
-(void)setMyView:(NSDictionary *)dic{
    
    if (dic ==nil) {
        return;
    }
    //第一行
    UIView *view1 = [[UIView alloc]initWithFrame:CGRectMake(0, 60, [[UIScreen mainScreen]bounds].size.width, 40)];
    view1.backgroundColor = [UIColor whiteColor];
    UIView *line1 = [[UIView alloc]initWithFrame:CGRectMake(0, view1.frame.size.height-1, view1.frame.size.width, 0.5)];
    line1.backgroundColor = [UIColor grayColor];
    [view1 addSubview:line1];
    [self.view addSubview:view1];
    UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, 60, view1.frame.size.height)];
    leftLabel.backgroundColor = [UIColor clearColor];
    leftLabel.textAlignment = NSTextAlignmentLeft;
    leftLabel.font = [UIFont systemFontOfSize:12.f];
    leftLabel.text = @"商家名称";
    [view1 addSubview:leftLabel];
    
    UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(70, 0, view1.frame.size.width-75, view1.frame.size.height)];
    rightLabel.backgroundColor = [UIColor clearColor];
    rightLabel.textAlignment = NSTextAlignmentRight;
    rightLabel.font = [UIFont systemFontOfSize:12.f];
    rightLabel.text =self.shopName;
    [view1 addSubview:rightLabel];
    
    
    
    
    UIView *view2 = [[UIView alloc]initWithFrame:CGRectMake(0, view1.frame.size.height+view1.frame.origin.y, [[UIScreen mainScreen]bounds].size.width, 0)];
    view2.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:view2];
    
    UILabel *line21 = [[UILabel alloc]initWithFrame:CGRectMake(5, 15, 150, 20)];
    line21.backgroundColor = [UIColor whiteColor];
    line21.font = [UIFont systemFontOfSize:12.f];
    line21.text = @"优惠券: ";
    [view2 addSubview:line21];
    CGSize size= [[dic objectForKey:@"function"] boundingRectWithSize:CGSizeMake(view2.frame.size.width-10, MAXFLOAT)
                                             options:NSStringDrawingUsesLineFragmentOrigin
                                          attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.f]}
                                             context:nil].size;
    UILabel *functionLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, line21.frame.origin.y+line21.frame.size.height, size.width, size.height)];
    functionLabel.text = [dic objectForKey:@"function"];
    functionLabel.textColor = [UIColor blackColor];
    functionLabel.font = [UIFont systemFontOfSize:12.f];
    functionLabel.numberOfLines = 0;
    functionLabel.backgroundColor = [UIColor clearColor];
    [view2 addSubview:functionLabel];
    CGRect rect = view2.frame;
    view2.frame = CGRectMake(0, rect.origin.y, rect.size.width, line21.frame.origin.y+line21.frame.size.height+functionLabel.frame.size.height+5);
    
    UIView *line2 = [[UIView alloc]initWithFrame:CGRectMake(0,  view2.frame.size.height-1, view2.frame.size.width, 0.5)];
    line2.backgroundColor = [UIColor grayColor];
    [view2 addSubview:line2];
    
    
    
    UIView *view3 = [[UIView alloc]initWithFrame:CGRectMake(0, view2.frame.size.height+view2.frame.origin.y, [[UIScreen mainScreen]bounds].size.width, 40)];
    view3.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:view3];
    
    
    
    
    
    UILabel *line31 = [[UILabel alloc]initWithFrame:CGRectMake(5, 15, 150, 20)];
    line31.backgroundColor = [UIColor clearColor];
    line31.font = [UIFont systemFontOfSize:12.f];
    line31.text = @"优惠券说明: ";
    [view3 addSubview:line31];
    
    size= [[dic objectForKey:@"remark"] boundingRectWithSize:CGSizeMake(view3.frame.size.width-10, MAXFLOAT)
                                    options:NSStringDrawingUsesLineFragmentOrigin
                                 attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.f]}
                                    context:nil].size;
    UILabel *remarkLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, line31.frame.origin.y+line31.frame.size.height, size.width, size.height)];
    remarkLabel.text = [dic objectForKey:@"remark"] ;
    remarkLabel.font = [UIFont systemFontOfSize:12.f];
    remarkLabel.backgroundColor = [UIColor clearColor];
    remarkLabel.textColor = [UIColor blackColor];
    remarkLabel.numberOfLines = 0;
    [view3 addSubview:remarkLabel];
    rect = view3.frame;
    
    view3.frame = CGRectMake(0, rect.origin.y, rect.size.width, line31.frame.origin.y+line31.frame.size.height+remarkLabel.frame.size.height+5);
    
    UIView *line3 = [[UIView alloc]initWithFrame:CGRectMake(0,  view3.frame.size.height-1, view3.frame.size.width, 0.3)];
    line3.backgroundColor = [UIColor grayColor];
    [view3 addSubview:line3];
    
    rect = view3.frame;
    
    
    
    UIView *view5 = [[UIView alloc]initWithFrame:CGRectMake(0, rect.origin.y+rect.size.height, [[UIScreen mainScreen]bounds].size.width, 40)];
    view5.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:view5];
    
    UILabel *leftLabel2 = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, 60, view5.frame.size.height)];
    leftLabel2.backgroundColor = [UIColor clearColor];
    leftLabel2.textAlignment = NSTextAlignmentLeft;
    leftLabel2.font = [UIFont systemFontOfSize:12.f];
    leftLabel2.text = @"支付";
    [view5 addSubview:leftLabel2];
    
    UILabel *rightLabel2 = [[UILabel alloc]initWithFrame:CGRectMake(70, 0, view5.frame.size.width-75, view5.frame.size.height)];
    rightLabel2.backgroundColor = [UIColor clearColor];
    rightLabel2.textAlignment = NSTextAlignmentRight;
    rightLabel2.font = [UIFont systemFontOfSize:12.f];
    rightLabel2.text =  [NSString stringWithFormat:@"%@元",[dic objectForKey:@"insteadPrice"]];
    [view5 addSubview:rightLabel2];
    
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    button.frame = CGRectMake(20, view5.frame.origin.y+view5.frame.size.height+10, view5.frame.size.width-40, 40);
    button.layer.cornerRadius = 3;
    button.layer.masksToBounds = YES;
    button.backgroundColor = [UIColor colorWithRed:182/255.0 green:0/255.0 blue:12/255.0 alpha:1];
    [button setTitle:@"在线支付" forState:UIControlStateNormal];
    button.titleLabel.font = [UIFont boldSystemFontOfSize:18.f];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.view addSubview:button];
    [button addTarget:self action:@selector(goPay) forControlEvents:UIControlEventTouchUpInside];
    

}

-(void)goPay{
    
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:[NSString stringWithFormat:@"%@",[self.couponDic objectForKey:@"insteadPrice"]] forKey:@"orderAmount"];
    [params setObject:self.userCouponCode forKey:@"userCouponCode"];
    [params setObject:@"0" forKey:@"platBonus"];
    [params setObject:@"0" forKey:@"shopBonus"];
    
    NSString* vcode = [gloabFunction getSign:@"bankcardPay" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak __typeof(self)weakSelf = self;

    [self.jsonPrcClient invokeMethod:@"bankcardPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {


        NSString *code = [responseObject objectForKey:@"code"];
        if ([code intValue] ==50000) {
            
            
            BMSQ_PayCardViewController *vc = [[BMSQ_PayCardViewController alloc]init];
            vc.fromVC =  weakSelf.formVc;
            vc.shopCode = self.shopCode;
            vc.payNewPrice = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"realPay"]];  //花费钱数
            vc.batchCouponCode = @"";
            vc.nbrCoupon =  @"0";  //优惠张数
            vc.platBonus = @"0";
            vc.shopBonus = @"0";
            vc.payType = @"1";   //支付方式
            vc.shopName = self.shopName;
            vc.type56 = YES;
            vc.orderNbr =[NSString stringWithFormat:@"%@", [responseObject objectForKey:@"orderNbr"]];
            vc.consumeCode =[NSString stringWithFormat:@"%@", [responseObject objectForKey:@"consumeCode"]];
            [self.navigationController pushViewController:vc animated:YES];
            
            
        }else{
            [SVProgressHUD showErrorWithStatus:@"优惠券过期或禁用"];
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
         
        
    }];
    
    
    
}


@end
