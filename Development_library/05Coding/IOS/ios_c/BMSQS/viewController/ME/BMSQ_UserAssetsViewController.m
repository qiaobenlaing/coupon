//
//  BMSQ_UserAssetsViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/27.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_UserAssetsViewController.h"

@interface BMSQ_UserAssetsViewController ()

@property (nonatomic, strong)NSDictionary *resultDic;

@property (nonatomic, strong)UILabel *platLabel;
@property (nonatomic, strong)UILabel *shopLabel;
@property (nonatomic, strong)UILabel *couponLabel;

@property (nonatomic, strong)UIView *remarkView;

@property (nonatomic, strong)UILabel *remarkLabel;


@end

@implementation BMSQ_UserAssetsViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"我的资产"];

  
    UIView *topView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 60)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:topView];
    
    for (int i=0; i<2; i++) {
        UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/3*(i+1), 15, 1, 30)];
        lineView.backgroundColor = self.view.backgroundColor;
        [topView addSubview:lineView];
    }
    
    
    self.platLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/3, 60)];
    self.platLabel.textAlignment = NSTextAlignmentCenter;
    self.platLabel.numberOfLines = 2;
    self.platLabel.textColor = APP_TEXTCOLOR;
    self.platLabel.font = [UIFont systemFontOfSize:13.f];
    [topView addSubview:self.platLabel];
    
    self.shopLabel = [[UILabel alloc]initWithFrame:CGRectMake( APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3, 60)];
    self.shopLabel.textAlignment = NSTextAlignmentCenter;
    self.shopLabel.numberOfLines = 2;
    self.shopLabel.textColor = APP_TEXTCOLOR;
    self.shopLabel.font = [UIFont systemFontOfSize:13.f];
    [topView addSubview:self.shopLabel];
    
    
    self.couponLabel= [[UILabel alloc]initWithFrame:CGRectMake( APP_VIEW_WIDTH/3*2, 0, APP_VIEW_WIDTH/3, 60)];
    self.couponLabel.textAlignment = NSTextAlignmentCenter;
    self.couponLabel.numberOfLines = 2;
    self.couponLabel.textColor = APP_TEXTCOLOR;
    self.couponLabel.font = [UIFont systemFontOfSize:13.f];
    [topView addSubview:self.couponLabel];
    
    
    UILabel *sectionLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, topView.frame.size.height+APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 40)];
    sectionLabel.backgroundColor = [UIColor clearColor];
    sectionLabel.font = [UIFont systemFontOfSize:15];
    sectionLabel.text = @"  惠币说明";
    [self.view addSubview:sectionLabel];
    
    self.remarkView = [[UIView alloc]initWithFrame:CGRectMake(0, sectionLabel.frame.origin.y+sectionLabel.frame.size.height, APP_VIEW_WIDTH, 0)];
    self.remarkView.backgroundColor = [UIColor whiteColor];
    
    [self.view addSubview:self.remarkView];
    
    
    self.remarkLabel = [[UILabel alloc]init];
    self.remarkLabel.backgroundColor = [UIColor whiteColor];
    self.remarkLabel.numberOfLines = 0;
    self.remarkLabel.textColor = APP_TEXTCOLOR;
    self.remarkLabel.font = [UIFont systemFontOfSize:12];
    [self.remarkView addSubview:self.remarkLabel];
    
    
    
    
    
    [self getMyAsset];
}

-(void)getMyAsset{
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    NSString* vcode = [gloabFunction getSign:@"getMyAsset" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:@""];
    
    [self.jsonPrcClient invokeMethod:@"getMyAsset" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];

        weakSelf.platLabel.text = [NSString stringWithFormat:@"%@\n惠币",[responseObject objectForKey:@"currCoin"]];
        weakSelf.shopLabel.text = [NSString stringWithFormat:@"%@\n红包",[responseObject objectForKey:@"userBonus"]];
        weakSelf.couponLabel.text = [NSString stringWithFormat:@"%@\n优惠券",[responseObject objectForKey:@"userCouponCount"]];
        weakSelf.remarkLabel.text = [responseObject objectForKey:@"coinIntro"];
        
        
        CGSize size = [[responseObject objectForKey:@"coinIntro"] boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT)
                                             options:NSStringDrawingUsesLineFragmentOrigin
                                          attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12]}
                                             context:nil].size;
        
        CGRect rect = weakSelf.remarkView.frame;
        rect.size.height = size.height+20;
        
        weakSelf.remarkView.frame = rect;
        
        weakSelf.remarkLabel.frame = CGRectMake(10, 10, size.width, size.height);
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
}

@end
