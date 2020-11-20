//
//  BMSQ_QiangRedViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/16.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_QiangRedViewController.h"

#import "SVProgressHUD.h"


#import "BMSQ_ShopDetailController.h"

@interface BMSQ_QiangRedViewController ()


@property (nonatomic, strong)UIImageView *popleImage;
@property (nonatomic, strong)UIImageView *headImageView;
@property (nonatomic, strong)NSString *shopCode;

@property (nonatomic,strong)UIImageView *grabgetView;
@property (nonatomic, strong)UIImageView *conentView;
@property (nonatomic, strong)UIView *successView;

@property (nonatomic, strong)UILabel *succPrice;


@end

@implementation BMSQ_QiangRedViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"抢红包"];
    
    [self setSomeView];

    [self requset];
    
    
    
}
-(void)setSomeView{
    UIImageView *backImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 300, 280)];
    [backImage setImage:[UIImage imageNamed:@"grabbouns"]];
    backImage.center = CGPointMake(APP_VIEW_WIDTH/2, 280);
    [self.view addSubview:backImage];
    
    self.popleImage = [[UIImageView alloc]initWithFrame:CGRectMake(30, 30, 200, 100)];
    [self.popleImage setImage:[UIImage imageNamed:@"grabface"]];
    [backImage addSubview:self.popleImage];
    
    self.headImageView = [[UIImageView alloc]initWithFrame:CGRectMake(10, APP_VIEW_HEIGHT-100, 70, 70)];
    self.headImageView.layer.borderColor = [[UIColor grayColor]CGColor];
    self.headImageView.layer.cornerRadius = 5;
    self.headImageView.layer.masksToBounds = YES;
    [self.view addSubview:self.headImageView];
    
    self.grabgetView = [[UIImageView alloc]initWithFrame:CGRectMake(80, 150, 150, 100)];
    [self.grabgetView setImage:[UIImage imageNamed:@"grabfinish"]];
    [backImage addSubview:self.grabgetView];
    self.grabgetView.hidden = YES;
    
    self.conentView = [[UIImageView alloc]initWithFrame:CGRectMake(80, 150, 150, 100)];
    [self.conentView setImage:[UIImage imageNamed:@"grabget"]];
    [backImage addSubview:self.conentView];
    self.conentView.hidden = YES;
    

    self.successView = [[UIView alloc]initWithFrame:CGRectMake(80, 150, 150, 100)];
        [backImage addSubview:self.successView];
    
    UIImageView *sussViwe = [[UIImageView alloc]initWithFrame:CGRectMake(self.successView.frame.size.width/2-30, 0, 60, 30)];
    [sussViwe setImage:[UIImage imageNamed:@"congratulation"]];
    [self.successView addSubview:sussViwe];
    
    
    UIImageView *succView2 =[[UIImageView alloc]initWithFrame:CGRectMake(0, 40,70, 30)];
    [succView2 setImage:[UIImage imageNamed:@"grabmoneybouns"]];
    [self.successView addSubview:succView2];
    
    self.succPrice = [[UILabel alloc]initWithFrame:CGRectMake(80, 35, 70, 35)];
    self.succPrice.backgroundColor = [UIColor clearColor];
    self.succPrice.textColor = [UIColor yellowColor];
    self.succPrice.font = [UIFont systemFontOfSize:30.f];
    [self.successView addSubview:self.succPrice];
    
    UIImageView *yuanImage = [[UIImageView alloc]initWithFrame:CGRectMake(self.succPrice.frame.origin.x+self.succPrice.frame.size.width, 40,30, 30)];
    [yuanImage setImage:[UIImage imageNamed:@"grabmoney"]];
    [self.successView addSubview:yuanImage];
    
    
    
    UIImageView *ImageImageView = [[UIImageView alloc]initWithFrame:CGRectMake(self.headImageView.frame.origin.x+self.headImageView.frame.size.width +10, self.headImageView.frame.origin.y, 50, 60)];
    [ImageImageView setImage:[UIImage imageNamed:@"grabgo"]];
    [self.view addSubview:ImageImageView];
    
    UIButton *gogobtn = [[UIButton alloc]initWithFrame:CGRectMake(ImageImageView.frame.origin.x+ImageImageView.frame.size.width +10, self.headImageView.frame.origin.y+30, 120, 30)];
    
    [gogobtn setImage:[UIImage imageNamed:@"grabgoto"] forState:UIControlStateNormal];
    
    [gogobtn addTarget:self action:@selector(gotoDetail) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:gogobtn];
    
    
    
    
    
}

-(void)gotoDetail{
    
    BMSQ_ShopDetailController* shopDetailCtrl = [[BMSQ_ShopDetailController alloc]init];
    shopDetailCtrl.shopCode = self.shopCode;
    [self.navigationController pushViewController:shopDetailCtrl animated:YES];

}
-(void)requset{
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.bonusCode forKey:@"bonusCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    NSString* vcode = [gloabFunction getSign:@"grabBonus" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"grabBonus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD showSuccessWithStatus:@"加载完成"];
        switch ([[responseObject objectForKey:@"code"]integerValue]) {
            case 50000:
                [wself.popleImage setImage:[UIImage imageNamed:@"grabface"]];
                wself.conentView.hidden = YES;
                wself.successView.hidden =NO;
                wself.succPrice.text = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"value"]];
                break;
                
            case 50714:
                [wself.popleImage setImage:[UIImage imageNamed:@"grabcryface"]];
                wself.conentView.hidden = NO;
                wself.successView.hidden = YES;
                break;
                
            case 50715:
                [wself.popleImage setImage:[UIImage imageNamed:@"grabcryface"]];
                wself.conentView.hidden = NO;
                wself.successView.hidden = YES;
                break;
                
            case 50717:
                [wself.popleImage setImage:[UIImage imageNamed:@"grabface"]];
                self.grabgetView.hidden =NO;
                wself.conentView.hidden = YES;
                wself.successView.hidden = YES;

                
            default:
                break;
        }
        
//        if ([[responseObject objectForKey:@"code"]integerValue] == 50000) {
//            [wself.popleImage setImage:[UIImage imageNamed:@"grabface"]];
//            wself.conentView.hidden = YES;
//            wself.successView.hidden =NO;
//            wself.succPrice.text = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"value"]];
//
//            
//        }else if(){
//            [wself.popleImage setImage:[UIImage imageNamed:@"grabcryface"]];
//            wself.conentView.hidden = NO;
//            wself.successView.hidden = YES;
//            
//        }

        NSString *str = [NSString stringWithFormat:@"%@/%@",IMAGE_URL,[responseObject objectForKey:@"logoUrl"]];
//        [wself.headImageView setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@""]];
        
        [wself.headImageView sd_setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        wself.shopCode = [responseObject objectForKey:@"shopCode"];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD showErrorWithStatus:@"加载失败，请查看网络"];
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"error" message:@"赶快登录账号吧" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
        
        [alterView show];
        
    }];
}

@end
