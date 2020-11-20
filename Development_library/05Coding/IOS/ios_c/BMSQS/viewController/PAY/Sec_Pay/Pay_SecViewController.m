//
//  Pay_SecViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/17.
//  Copyright (c) 2015年 djx. All rights reserved.
//
 
#import "Pay_SecViewController.h"

#import "SVProgressHUD.h"

@interface Pay_SecViewController ()


@property (nonatomic, strong)NSMutableDictionary *couponDic;

@property (nonatomic, strong)UIButton *sumButton;
@property (nonatomic, strong)UIImageView *imageView;

@property (nonatomic, strong)UILabel *nameLabel;
@property (nonatomic, strong)UILabel *dateLabel;
@property (nonatomic, strong)UILabel *timeLabel;
@property (nonatomic, strong)UIImageView *shopPic;

@end

@implementation Pay_SecViewController

- (void)getUserCouponInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.userCouponCode forKey:@"userCouponCode"];
    NSString* vcode = [gloabFunction getSign:@"getUserCouponInfo" strParams:self.userCouponCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getUserCouponInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        
        
        if ([responseObject objectForKey:@"shopName"]) {
            weakSelf.nameLabel.text = [responseObject objectForKey:@"function"];
            weakSelf.dateLabel.text =[NSString stringWithFormat:@"%@～%@",[responseObject objectForKey:@"startUsingTime"],[responseObject objectForKey:@"expireTime"]];
            weakSelf.timeLabel.text =  [NSString stringWithFormat:@"使用时间:%@-%@",[responseObject objectForKey:@"dayStartUsingTime"],[responseObject objectForKey:@"dayEndUsingTime"]] ;
            NSString *str = [NSString stringWithFormat:@"%@%@",IMAGE_URL,self.imageUrl];
//            [weakSelf.shopPic setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv_loadingLogo"]];
            
            [weakSelf.shopPic sd_setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];


        }
        
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        
    }];
}



- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:self.myTitle];
    [self getUserCouponInfo];
    self.couponDic = [[NSMutableDictionary alloc]init];
    
    UIView *couponView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH, 90)];
    couponView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:couponView];
    
    self.shopPic = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 50, 50)];
    self.shopPic.layer.masksToBounds = YES;
    self.shopPic.layer.cornerRadius = 3;
    self.shopPic.layer.borderWidth = 0.4;
    self.shopPic.layer.borderColor = [[UIColor grayColor]CGColor];
    NSString *str = [NSString stringWithFormat:@"%@%@",IMAGE_URL,self.imageUrl];
    [self.shopPic sd_setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv_loadingLogo"]];
    [couponView addSubview:self.shopPic];
    
    self.nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(70, 5, APP_VIEW_WIDTH-80, 30)];
    self.nameLabel.backgroundColor = [UIColor clearColor];
    self.nameLabel.font = [UIFont boldSystemFontOfSize:13.f];
    self.nameLabel.numberOfLines = 2;
    self.nameLabel.text = [self.couponDic objectForKey:@"function"];
    [couponView addSubview:self.nameLabel];
//
    self.dateLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.nameLabel.frame.origin.x, 40, self.nameLabel.frame.size.width, 20)];
    self.dateLabel.backgroundColor = [UIColor clearColor];
    self.dateLabel.font = [UIFont systemFontOfSize:11.f];
    self.dateLabel.numberOfLines = 2;
    self.dateLabel.text =[NSString stringWithFormat:@"%@～%@",[self.couponDic objectForKey:@"startUsingTime"],[self.couponDic objectForKey:@"expireTime"]];
    [couponView addSubview:self.dateLabel];
//
    self.timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.nameLabel.frame.origin.x, 60, self.nameLabel.frame.size.width, 20)];
    self.timeLabel.backgroundColor = [UIColor clearColor];
    self.timeLabel.font = [UIFont systemFontOfSize:11.f];
    self.timeLabel.numberOfLines = 2;
    self.timeLabel.text =  [NSString stringWithFormat:@"使用时间:%@-%@",[self.couponDic objectForKey:@"dayStartUsingTime"],[self.couponDic objectForKey:@"dayEndUsingTime"]] ; ;
    [couponView addSubview:self.timeLabel];
    
    
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, couponView.frame.origin.y+couponView.frame.size.height+30, APP_VIEW_WIDTH, 40)];
    label.backgroundColor = [UIColor clearColor];
    label.textAlignment = NSTextAlignmentCenter;
    label.textColor = [UIColor colorWithRed:224/255.0 green:61/255.0 blue:0/255.0 alpha:1];
    label.font = [UIFont boldSystemFontOfSize:22];
    label.text = @"您确定要用掉我吗？";
    [self.view addSubview:label];
    
    
    UILabel *label1 = [[UILabel alloc]initWithFrame:CGRectMake(0, label.frame.origin.y+label.frame.size.height+5, APP_VIEW_WIDTH, 20)];
    label1.backgroundColor = [UIColor clearColor];
    label1.textAlignment = NSTextAlignmentCenter;
    label1.textColor = [UIColor grayColor];
    label1.font = [UIFont systemFontOfSize:14];
    label1.text = @"请在收银员的指导下点击确认使用";
    [self.view addSubview:label1];
    
    
    self.sumButton = [[UIButton alloc]initWithFrame:CGRectMake(10, label1.frame.origin.y+label1.frame.size.height+30, APP_VIEW_WIDTH-20, 40)];
    self.sumButton.layer.masksToBounds = YES;
    self.sumButton.layer.cornerRadius = 5;
    self.sumButton.backgroundColor = [UIColor colorWithRed:179/255.0 green:0/255.0 blue:12/255.0 alpha:1];
    [self.sumButton setTitle:@"确认使用" forState:UIControlStateNormal];
    [self.sumButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.sumButton.titleLabel.font = [UIFont systemFontOfSize:18];
    [self.view addSubview:self.sumButton];
    [self.sumButton addTarget:self action:@selector(submitUse) forControlEvents:UIControlEventTouchUpInside];
    
    
    self.imageView = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-150, self.sumButton.frame.origin.y-50, 120, 120)];
    self.imageView.backgroundColor = [UIColor clearColor];
    [self.imageView setImage:[UIImage imageNamed:@"define_use"]];
    [self.view addSubview:self.imageView];
    self.imageView.hidden = YES;
    
    
}

-(void)submitUse{

    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.userCouponCode forKey:@"userCouponCode"];
    
    NSString* vcode = [gloabFunction getSign:@"zeroPay" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"zeroPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        if ([responseObject objectForKey:@"code"]) {
            
            if ([[responseObject objectForKey:@"code"]integerValue] == 50000) {
                wself.sumButton.enabled = NO;
                wself.sumButton.backgroundColor = [UIColor grayColor];
                wself.imageView.hidden = NO;
            }else{
                
                
                 NSString *code =[responseObject objectForKey:@"code"];
                int i =[code intValue];
                switch (i) {
                    case 50314:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"商家编码错误",code]];
                        break;
                    case 50317:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"请输入商家编码",code]];
                        break;
                    case 50400:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"请输入消费金额",code]];
                        break;
                    case 50401:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"消费金额不正确",code]];
                        break;
                    case 50500:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"消请输入用户编码",code]];
                        break;
                    case 50503:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"用户编码错误",code]];
                        break;
                    case 50720:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"红包不可用",code]];
                        break;
                    case 50724:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"红包已经过期",code]];
                        break;
                    case 60501:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"订单不存在",code]];
                        break;
                    case 80220:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"优惠券已经过期",code]];
                        break;
                    case 80227:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"优惠券不可用",code]];
                        break;
                    case 80400:
                        [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"用户会员卡不可用",code]];
                        break;
                    default:
                        [self showAlertView:[NSString stringWithFormat:@"错误：请联系相关人员[%@]",code]];
                        break;
            }
                
                wself.sumButton.enabled = NO;
                wself.sumButton.backgroundColor = [UIColor grayColor];
                wself.imageView.hidden = NO;

             }
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

      
        
    }];
    
    
    
}

@end
