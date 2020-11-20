//
//  BMSQ_CouponViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/13.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponViewController.h"

#import "BMSQ_CouponListViewController.h"
#import "BMSQ_CouponReceiveViewController.h"
#import "BMSQ_CouponTypeViewController.h"
#import "BMSQ_CouponDetailViewController.h"
#import "PNColor.h"

#import "SVProgressHUD.h"
#import "PNBarChart.h"
#import "UIColor+Tools.h"

#import <ShareSDK/ShareSDK.h>
#import "HQShareUtils.h"
#import "CouponTypeModel.h"
#import "BMSQ_couponSetViewController.h"


@interface BMSQ_CouponViewController ()


@property (nonatomic, strong)NSDictionary *resultDic;
@property (nonatomic, strong)UIScrollView *mainScView;

@end

@implementation BMSQ_CouponViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
    NSString *shopName = [uD objectForKey:@"shopName"];
    [self setNavTitle:shopName];
    
    
    UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-80 ,  20, 80, 50)];
    button.backgroundColor = [UIColor clearColor];
    [button setImage:[UIImage imageNamed:@"right_add"] forState:UIControlStateNormal];
    [button setImageEdgeInsets:UIEdgeInsetsMake(5, 40, 5, 0)];
    [button addTarget:self action:@selector(rightButtonClicked) forControlEvents:UIControlEventTouchUpInside];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    
    
    [self setRight:button];
    
    
    self.mainScView = [[UIScrollView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-self.tabBarController.tabBar.frame.size.height)];
    [self.view addSubview:self.mainScView];
    
//    [self setSelfView];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(refrshCoupon) name:@"refrshCoupon" object:nil];

    
}
-(void)refrshCoupon{
    for (UIView *v in self.mainScView.subviews) {
        [v removeFromSuperview];
    }
    [self getCouponHomePage];

}
-(void)rightButtonClicked{
    

//    NSArray *status = @[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券", @"兑换券", @"代金券"];
//    int type = 3;
//    
//    BMSQ_couponSetViewController *vc = [[BMSQ_couponSetViewController alloc]init];
//    vc.myTitle = [status objectAtIndex:type];
//    vc.type = type;
//    vc.hidesBottomBarWhenPushed = YES;
//    [self.navigationController pushViewController:vc animated:YES];
    
    
    
    BMSQ_CouponTypeViewController *vc = [[BMSQ_CouponTypeViewController alloc]init];
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
}




-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
    NSString *shopName = [uD objectForKey:@"shopName"];
    [self setNavTitle:shopName];
    
    if (self.resultDic.count==0) {
        [self getCouponHomePage];
    }else{
        
    }
    

}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [SVProgressHUD dismiss];
    
}


-(void)setSelfView{

    NSDictionary *couponInfo = [self.resultDic objectForKey:@"recentCouponInfo"];
    if (couponInfo.count>0) {
        UIView *couponView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/5)];
        couponView.backgroundColor = [UIColor clearColor];
        [self.mainScView addSubview:couponView];
        
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(gotoCouponDetail)];
        couponView.userInteractionEnabled = YES;
        [couponView addGestureRecognizer:tapGesture];
        
        
        NSString *type = [NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"couponType"]];
        if (type.intValue < 9) {
            NSDictionary *couponDic = [CouponTypeModel createCoupon:type.intValue];
            
            NSString *imageName = [couponDic objectForKey:@"image"];
            NSString *backColor = [couponDic objectForKey:@"backColor"];
            NSString *bottomColor = [couponDic objectForKey:@"bottomColor"];
            NSString *couponTypeStr = [couponDic objectForKey:@"status"];
            
            
            
            
            UIView *baView = [[UIView alloc]initWithFrame:CGRectMake(8, 0, APP_VIEW_WIDTH-16, couponView.frame.size.height-8)];
            baView.backgroundColor = [UIColor whiteColor];
            [couponView addSubview:baView];
            
            NSArray *backColors = [backColor componentsSeparatedByString:@","];
            UIImageView *backImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, baView.frame.size.width/4, baView.frame.size.height)];
            backImage.backgroundColor = UICOLOR([[backColors objectAtIndex:0]floatValue], [[backColors objectAtIndex:1] floatValue], [[backColors objectAtIndex:2] floatValue], 1);
            [baView addSubview:backImage];
            UIImageView *typeImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, backImage.frame.size.width-30, backImage.frame.size.height-30)];
            typeImage.center = CGPointMake(backImage.frame.size.width/2, backImage.frame.size.height/2-10) ;
            typeImage.backgroundColor = [UIColor clearColor];
            [typeImage setImage:[UIImage imageNamed:imageName]];
            [backImage addSubview:typeImage];
            
            UILabel *insteadPriceLabel = [[UILabel alloc] initWithFrame:typeImage.frame];
            insteadPriceLabel.textAlignment = NSTextAlignmentCenter;
            insteadPriceLabel.backgroundColor = [UIColor clearColor];
            insteadPriceLabel.font = [UIFont systemFontOfSize:15.f];
            insteadPriceLabel.textColor = [UIColor whiteColor];
            [backImage addSubview:insteadPriceLabel];
            
            
            
            backColors = [bottomColor componentsSeparatedByString:@","];
            UILabel *bottomLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, backImage.frame.size.height-20, backImage.frame.size.width, 20)];
            bottomLabel.backgroundColor =UICOLOR([[backColors objectAtIndex:0]floatValue], [[backColors objectAtIndex:1] floatValue], [[backColors objectAtIndex:2] floatValue], 1);
            bottomLabel.textColor = [UIColor whiteColor];
            bottomLabel.font = [UIFont systemFontOfSize:11.f];
            bottomLabel.textAlignment = NSTextAlignmentCenter;
            bottomLabel.text = couponTypeStr;
            [backImage addSubview:bottomLabel];
            
            
            UIImageView *unavaImageV = [[UIImageView alloc]initWithFrame:backImage.bounds];
            [unavaImageV setImage:[UIImage imageNamed:@"unavailable"]];
            [backImage addSubview:unavaImageV];
            
            if([[couponInfo objectForKey:@"isExpire"]intValue] ==0){
                unavaImageV.hidden = NO;
            }else{
                unavaImageV.hidden = YES;
                
            }
            
            
            UILabel *piciLabel = [[UILabel alloc]initWithFrame:CGRectMake(backImage.frame.size.width , 0 , baView.frame.size.width-backImage.frame.size.width, baView.frame.size.height/3)];
            piciLabel.text = [NSString stringWithFormat:@"  批次:%@",[couponInfo objectForKey:@"batchNbr"]];
            piciLabel.font = [UIFont systemFontOfSize:14.f];
            piciLabel.backgroundColor = [UIColor clearColor];
            [baView addSubview:piciLabel];
            
            UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(backImage.frame.size.width , piciLabel.frame.size.height, baView.frame.size.width-backImage.frame.size.width-100, baView.frame.size.height/3)];
            nameLabel.text = [NSString stringWithFormat:@"  %@",[couponInfo objectForKey:@"function"]];
            nameLabel.font = [UIFont systemFontOfSize:13.f];
            nameLabel.backgroundColor = [UIColor clearColor];
            [baView addSubview:nameLabel];
            insteadPriceLabel.text = [NSString stringWithFormat:@"Ұ %@",[couponInfo objectForKey:@"insteadPrice"]];
            if (type.intValue == 3) {
                nameLabel.text = [NSString stringWithFormat:@"  满%.2f元立减%.2f元",[[couponInfo objectForKey:@"availablePrice"] floatValue],[[couponInfo objectForKey:@"insteadPrice"] floatValue]];
                
                
            }else if(type.intValue == 4) {
                insteadPriceLabel.text = [NSString stringWithFormat:@"%0.1f折",[[couponInfo objectForKey:@"discountPercent"] floatValue]];
                nameLabel.text = [NSString stringWithFormat:@"  满%.2f元打%0.1f折",[[couponInfo objectForKey:@"availablePrice"] floatValue],[[couponInfo objectForKey:@"discountPercent"] floatValue]];
                
            }else if (type.intValue == 8) {
                nameLabel.text = [NSString stringWithFormat:@"  %.2f元代%.2f元", [[couponInfo objectForKey:@"payPrice"] floatValue], [[couponInfo objectForKey:@"insteadPrice"] floatValue]];
                
            }else{
                nameLabel.text = [NSString stringWithFormat:@"  %@",[couponInfo objectForKey:@"function"]];
                
            }
            
            
            UILabel *proBackLable = [[UILabel alloc]initWithFrame:CGRectMake(backImage.frame.size.width+10, nameLabel.frame.origin.y+nameLabel.frame.size.height, nameLabel.frame.size.width, 10)];
            proBackLable.layer.masksToBounds = YES;
            proBackLable.layer.cornerRadius = 8;
            proBackLable.layer.borderWidth = 0.5;
            proBackLable.layer.borderColor = [[UIColor grayColor]CGColor];
            [baView addSubview:proBackLable];
            
            float w = [[couponInfo objectForKey:@"takenCount"]floatValue]/[[couponInfo objectForKey:@"totalVolume"]floatValue]*(proBackLable.frame.size.width-10);
            UILabel *proBackLable1 = [[UILabel alloc]initWithFrame:CGRectMake(5,2, w,6)];
            proBackLable1.backgroundColor = UICOLOR(182, 0, 12, 1.0);
            proBackLable1.layer.masksToBounds = YES;
            proBackLable1.layer.cornerRadius = 6;
            [proBackLable addSubview:proBackLable1];
            
            NSString *totalVolume;
            
            
            UILabel *proLabel = [[UILabel alloc]initWithFrame:CGRectMake(backImage.frame.size.width , baView.frame.size.height-20 , baView.frame.size.width-backImage.frame.size.width,15)];

            proLabel.font = [UIFont systemFontOfSize:12.f];
            proLabel.backgroundColor = [UIColor clearColor];
            [baView addSubview:proLabel];
            
            
            if ([[couponInfo objectForKey:@"totalVolume"] isEqual:@"-1"]) {
                totalVolume  = @"无限制";
                proBackLable1.hidden = YES;
                proBackLable.hidden = YES;
                NSString *proLabelText = @"";
                int isAvailable  = [[couponInfo objectForKey:@"isAvailable"] intValue];
                if (isAvailable) { //是否停用
                    proLabelText = [NSString stringWithFormat:@"领取张数: %@",[couponInfo objectForKey:@"takenCount"]];
                }else {
                    proLabelText = [NSString stringWithFormat:@"已停用: %@",[couponInfo objectForKey:@"takenCount"]];
                }
                proLabel.text = proLabelText;
                CGRect proFrame = proLabel.frame;

                proLabel.frame = proFrame;
                
            }else {
                totalVolume = [couponInfo objectForKey:@"totalVolume"];
                NSString *proLabelText = @"";
                int isAvailable  = [[couponInfo objectForKey:@"isAvailable"] intValue];
                if (isAvailable) { //是否停用
                    proLabelText = [NSString stringWithFormat:@" 已领取(%@/%@)",[couponInfo objectForKey:@"takenCount"],totalVolume];
                }else {
                    proLabelText = [NSString stringWithFormat:@" 已停用(%@/%@)",[couponInfo objectForKey:@"takenCount"],totalVolume];
                }
                proLabel.text = proLabelText;
                CGRect proFrame = proLabel.frame;
                proLabel.frame = proFrame;
                proLabel.frame = proFrame;
            }
            
            

            
            
            
            UIButton *shareBtn = [[UIButton alloc]initWithFrame:CGRectMake(nameLabel.frame.origin.x+nameLabel.frame.size.width+3, nameLabel.frame.origin.y, 30, 30)];
            shareBtn.tag = 1001;
            shareBtn.backgroundColor = [UIColor clearColor];
            [shareBtn setImage:[UIImage imageNamed:@"iv_share"] forState:UIControlStateNormal];
            [shareBtn addTarget:self action:@selector(shareClick) forControlEvents:UIControlEventTouchUpInside];
            [baView addSubview:shareBtn];
            
            if ([[couponInfo objectForKey:@"isSend"] intValue]==1) {
                shareBtn.hidden = YES;
            } else {
                shareBtn.hidden = NO;
            }
            
            
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(shareBtn.frame.origin.x+shareBtn.frame.size.width+3, nameLabel.frame.origin.y, baView.frame.size.width-shareBtn.frame.origin.x -40, 30)];
            button.backgroundColor = [UIColor whiteColor];
            button.layer.borderWidth = 0.8;
            button.layer.cornerRadius = 5;
            button.layer.masksToBounds = YES;
            button.layer.borderColor = [[UIColor redColor]CGColor];
            [button setTitle:@"领取人员" forState:UIControlStateNormal];
            [button setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
            button.titleLabel.font = [UIFont systemFontOfSize:12.f];
            [button addTarget:self action:@selector(ReceiveClick) forControlEvents:UIControlEventTouchUpInside];
            
            [baView addSubview:button];
            
            
            [self couponListView:couponView];
            
        }
        
 
    }else{
        UIView *couponView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 0)];
        couponView.backgroundColor = [UIColor redColor];
        [self.mainScView addSubview:couponView];
        [self couponListView:couponView];
    }
   
    
}
-(void)couponListView:(UIView *)beforeView{
    UIView *couponListView = [[UIView alloc]initWithFrame:CGRectMake(0, beforeView.frame.origin.y+beforeView.frame.size.height, APP_VIEW_WIDTH, 45)];
    couponListView.backgroundColor = [UIColor whiteColor];
    [self.mainScView addSubview:couponListView];
    
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH-60, couponListView.frame.size.height)];
    label.text = @"  我发布的优惠券";
    label.font = [UIFont systemFontOfSize:14.f];
    [couponListView addSubview:label];
    
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-50, 0, 10, 15)];
    [imageView setImage:[UIImage imageNamed:@"iv_Right"]];
    [couponListView addSubview:imageView];
    imageView.center = CGPointMake(APP_VIEW_WIDTH-30, 45/2);
    
    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(gotoCouponList)];
    [couponListView addGestureRecognizer:tapGesture];
    
    [self couponMessageView:couponListView];
    
}
-(void)couponMessageView:(UIView *)beforeView{
    UIView *couponMessView = [[UIView alloc]initWithFrame:CGRectMake(0, beforeView.frame.origin.y+beforeView.frame.size.height+8, APP_VIEW_WIDTH, 45*5)];
    couponMessView.backgroundColor = [UIColor clearColor];
    [self.mainScView addSubview:couponMessView];
    NSDictionary *messDic = [self.resultDic objectForKey:@"couponStatistics"];
    
    NSArray *titleS = @[@"发放优惠券批次",@"共抵扣金额",@"带来消费金额",@"带来消费人次",@"当前未使用优惠券"];
    NSMutableArray *messageArr = [[NSMutableArray alloc]init];
    [messageArr addObject:[NSString stringWithFormat:@"%@次",[messDic objectForKey:@"nbrOfBatch"]]];
    [messageArr addObject:[NSString stringWithFormat:@"%.2f元",[[messDic objectForKey:@"nbrOfDeductionPrice"] floatValue]]];
    [messageArr addObject:[NSString stringWithFormat:@"%.2f元",[[messDic objectForKey:@"totalPrice"] floatValue]]];
    [messageArr addObject:[NSString stringWithFormat:@"%@次",[messDic objectForKey:@"totalPersonAmount"]]];
    [messageArr addObject:[NSString stringWithFormat:@"%@张",[messDic objectForKey:@"restOfCoupon"]]];


    
    for (int i=0; i<5; i++) {
        UIView *cellView = [[UIView alloc]initWithFrame:CGRectMake(0, i*45, APP_VIEW_WIDTH, 44)];
        cellView.backgroundColor = [UIColor whiteColor];
        [couponMessView addSubview:cellView];
        UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, APP_VIEW_WIDTH/2, cellView.frame.size.height)];
        leftLabel.text = [titleS objectAtIndex:i];
        leftLabel.font = [UIFont systemFontOfSize:14.f];
        [cellView addSubview:leftLabel];
        
        UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2-10, cellView.frame.size.height)];
        rightLabel.text =[messageArr objectAtIndex:i];
        rightLabel.font = [UIFont systemFontOfSize:14.f];
        rightLabel.textAlignment = NSTextAlignmentRight;
        
        [cellView addSubview:rightLabel];
        
    }
    
  
    [self ConsumptionView:couponMessView];
    
}
-(void)ConsumptionView:(UIView *)beforeView{
    UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, beforeView.frame.origin.y+beforeView.frame.size.height, APP_VIEW_WIDTH, 50)];
    titleLabel.backgroundColor = [UIColor clearColor];
    titleLabel.text = @"   每批次消费走势图";
    titleLabel.font = [UIFont systemFontOfSize:15.f];
    [self.mainScView addSubview:titleLabel];

    UIView *DrawingView = [[UIView alloc]initWithFrame:CGRectMake(0, titleLabel.frame.origin.y+titleLabel.frame.size.height, APP_VIEW_WIDTH, 210)];
    DrawingView.backgroundColor = [UIColor whiteColor];
    NSArray *arrays = [self.resultDic objectForKey:@"CouponConsumptionTrendInfo"];
    
    if(arrays.count>0){
        PNBarChart * lineChart = [[PNBarChart alloc] initWithFrame:CGRectMake(0,0, 300, 200.0)];
        lineChart.backgroundColor = [UIColor clearColor];
        NSMutableArray *XLabels = [[NSMutableArray alloc]init];
        for (NSDictionary *dic in arrays) {
            [XLabels addObject:[dic objectForKey:@"batchNbr"]];
        }
        
        NSMutableArray *YLabels = [[NSMutableArray alloc]init];
        for (NSDictionary *dic in arrays) {
            [YLabels addObject:[dic objectForKey:@"amount"]];
        }
        lineChart.showChartBorder = YES;
        [lineChart setXLabels:XLabels];
        [lineChart setYValues:YLabels];
        lineChart.isGradientShow = NO;
        lineChart.isShowNumbers = NO;
        [lineChart strokeChart];
        [DrawingView addSubview:lineChart];
        [self.mainScView addSubview:DrawingView];
        
        
        
        UILabel *label_square = [[UILabel alloc]initWithFrame:CGRectMake(10, 186, 10, 10)];
        label_square.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        label_square.font = [UIFont systemFontOfSize:12.f];
        [DrawingView addSubview:label_square];
        
        UILabel *label_money = [[UILabel alloc]initWithFrame:CGRectMake(25, 180, 60, 20)];
        label_money.backgroundColor = [UIColor clearColor];
        label_money.text = @"消费金额";
        label_money.font = [UIFont systemFontOfSize:12.f];
        label_money.textColor = UICOLOR(182, 0, 12, 1.0);
        [DrawingView addSubview:label_money];
        
        
        UILabel *xlabelas = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-30, 180, 60, 20)];
        xlabelas.backgroundColor = [UIColor clearColor];
        xlabelas.text = @"批次/批";
        xlabelas.textAlignment = NSTextAlignmentCenter;
        xlabelas.font = [UIFont systemFontOfSize:12.f];
        [DrawingView addSubview:xlabelas];
        
    }else{
        
        PNBarChart * lineChart = [[PNBarChart alloc] initWithFrame:CGRectMake(0,0, 300, 200.0)];
        lineChart.backgroundColor = [UIColor clearColor];
        NSMutableArray *XLabels = [[NSMutableArray alloc]initWithObjects:@"0", nil];
        NSMutableArray *YLabels = [[NSMutableArray alloc]initWithObjects:@"0", nil];

        lineChart.showChartBorder = YES;
        [lineChart setXLabels:XLabels];
        [lineChart setYValues:YLabels];
        lineChart.isGradientShow = NO;
        lineChart.isShowNumbers = NO;
        [lineChart strokeChart];
        [DrawingView addSubview:lineChart];
        [self.mainScView addSubview:DrawingView];
        
        
        
        UILabel *label_square = [[UILabel alloc]initWithFrame:CGRectMake(10, 186, 10, 10)];
        label_square.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        label_square.font = [UIFont systemFontOfSize:12.f];
        [DrawingView addSubview:label_square];
        
        UILabel *label_money = [[UILabel alloc]initWithFrame:CGRectMake(25, 180, 60, 20)];
        label_money.backgroundColor = [UIColor clearColor];
        label_money.text = @"消费金额";
        label_money.font = [UIFont systemFontOfSize:12.f];
        label_money.textColor = UICOLOR(182, 0, 12, 1.0);
        [DrawingView addSubview:label_money];
        
        
        UILabel *xlabelas = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-30, 180, 60, 20)];
        xlabelas.backgroundColor = [UIColor clearColor];
        xlabelas.text = @"批次/批";
        xlabelas.textAlignment = NSTextAlignmentCenter;
        xlabelas.font = [UIFont systemFontOfSize:12.f];
        [DrawingView addSubview:xlabelas];
        
    }
    
    [self ConsumptionPeopleView:DrawingView];
    
}
-(void)ConsumptionPeopleView:(UIView *)beforeView{
    UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, beforeView.frame.origin.y+beforeView.frame.size.height, APP_VIEW_WIDTH, 50)];
    titleLabel.backgroundColor = [UIColor clearColor];
    titleLabel.text = @"   每批次消费人次走势图";
    titleLabel.font = [UIFont systemFontOfSize:15.f];
    [self.mainScView addSubview:titleLabel];
    
    UIView *DrawingView = [[UIView alloc]initWithFrame:CGRectMake(0, titleLabel.frame.origin.y+titleLabel.frame.size.height, APP_VIEW_WIDTH, 210)];
    DrawingView.backgroundColor = [UIColor whiteColor];

    NSArray *arrays = [self.resultDic objectForKey:@"CouponConsumptionPersonTrend"];
    
    if(arrays.count>0){
        PNBarChart * lineChart = [[PNBarChart alloc] initWithFrame:CGRectMake(0,0, 300, 200.0)];
        lineChart.backgroundColor = [UIColor clearColor];
        NSMutableArray *XLabels = [[NSMutableArray alloc]init];
        for (NSDictionary *dic in arrays) {
            [XLabels addObject:[dic objectForKey:@"batchNbr"]];
        }
        
        NSMutableArray *YLabels = [[NSMutableArray alloc]init];
        for (NSDictionary *dic in arrays) {
            [YLabels addObject:[dic objectForKey:@"amount"]];
        }

        lineChart.showChartBorder = YES;
        [lineChart setXLabels:XLabels];
        [lineChart setYValues:YLabels];
        lineChart.isGradientShow = NO;
        lineChart.isShowNumbers = NO;
        [lineChart strokeChart];
        [DrawingView addSubview:lineChart];
        [self.mainScView addSubview:DrawingView];
   
        
        UILabel *label_square = [[UILabel alloc]initWithFrame:CGRectMake(10, 186, 10, 10)];
        label_square.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        label_square.font = [UIFont systemFontOfSize:12.f];
        [DrawingView addSubview:label_square];
        
        UILabel *label_money = [[UILabel alloc]initWithFrame:CGRectMake(25, 180, 60, 20)];
        label_money.backgroundColor = [UIColor clearColor];
        label_money.text = @"人数";
        label_money.font = [UIFont systemFontOfSize:12.f];
        label_money.textColor = UICOLOR(182, 0, 12, 1.0);
        [DrawingView addSubview:label_money];
        
        
        UILabel *xlabelas = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-30, 180, 60, 20)];
        xlabelas.backgroundColor = [UIColor clearColor];
        xlabelas.text = @"批次/批";
        xlabelas.textAlignment = NSTextAlignmentCenter;
        xlabelas.font = [UIFont systemFontOfSize:12.f];
        [DrawingView addSubview:xlabelas];
        
    }else{
        
        PNBarChart * lineChart = [[PNBarChart alloc] initWithFrame:CGRectMake(0,0, 300, 200.0)];
        lineChart.backgroundColor = [UIColor clearColor];
        NSMutableArray *XLabels = [[NSMutableArray alloc]initWithObjects:@"0", nil];
        NSMutableArray *YLabels = [[NSMutableArray alloc]initWithObjects:@"0", nil];
        
        lineChart.showChartBorder = YES;
        [lineChart setXLabels:XLabels];
        [lineChart setYValues:YLabels];
        lineChart.isGradientShow = NO;
        lineChart.isShowNumbers = NO;
        [lineChart strokeChart];
        [DrawingView addSubview:lineChart];
        [self.mainScView addSubview:DrawingView];
        
        
        UILabel *label_square = [[UILabel alloc]initWithFrame:CGRectMake(10, 186, 10, 10)];
        label_square.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        label_square.font = [UIFont systemFontOfSize:12.f];
        [DrawingView addSubview:label_square];
        
        UILabel *label_money = [[UILabel alloc]initWithFrame:CGRectMake(25, 180, 60, 20)];
        label_money.backgroundColor = [UIColor clearColor];
        label_money.text = @"人数";
        label_money.font = [UIFont systemFontOfSize:12.f];
        label_money.textColor = UICOLOR(182, 0, 12, 1.0);
        [DrawingView addSubview:label_money];
        
        
        UILabel *xlabelas = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-30, 180, 60, 20)];
        xlabelas.backgroundColor = [UIColor clearColor];
        xlabelas.text = @"批次/批";
        xlabelas.textAlignment = NSTextAlignmentCenter;
        xlabelas.font = [UIFont systemFontOfSize:12.f];
        [DrawingView addSubview:xlabelas];
    }

    
    [self.mainScView addSubview:DrawingView];
    self.mainScView.contentSize = CGSizeMake(APP_VIEW_WIDTH, DrawingView.frame.origin.y+DrawingView.frame.size.height);

    
}
#pragma mark --发布的优惠券列表--
-(void)gotoCouponList{
  
    BMSQ_CouponListViewController *vc = [[BMSQ_CouponListViewController alloc]init];
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}

#pragma mark --领取--
-(void)ReceiveClick{
    
    NSDictionary *couponInfo = [self.resultDic objectForKey:@"recentCouponInfo"];
    BMSQ_CouponReceiveViewController *vc = [[BMSQ_CouponReceiveViewController alloc]init];
    vc.batchCouponCode = [couponInfo objectForKey:@"batchCouponCode"];
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];

    
}

-(void)gotoCouponDetail{
    BMSQ_CouponDetailViewController *vc = [[BMSQ_CouponDetailViewController alloc]init];
    NSDictionary *couponInfo = [self.resultDic objectForKey:@"recentCouponInfo"];
    vc.batchCouponCode = [couponInfo objectForKey:@"batchCouponCode"];
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}

#pragma mark --请求数据--
-(void)getCouponHomePage{
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"getCouponHomePage" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getCouponHomePage" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        weakSelf.resultDic = responseObject;
        [weakSelf setSelfView];
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
}

#pragma mark --分享--
-(void)shareClick{
    NSLog(@"--------- >首页优惠券分享");
    
    NSDictionary *dicShare = [self.resultDic objectForKey:@"recentCouponInfo"];
    
    
    NSNumber *couponType = [dicShare objectForKey:@"couponType"];
    
    NSString *str;
    
    if (couponType.intValue == 1)
    {
        str = [NSString stringWithFormat:@"%@",[dicShare objectForKey:@"function"]];
        
    } else if (couponType.intValue == 3) {
        str = [NSString stringWithFormat:@"满%@元立减%@元",[dicShare objectForKey:@"availablePrice"],[dicShare objectForKey:@"insteadPrice"]];
        
    }else if (couponType.intValue == 4){
        
        str = [NSString stringWithFormat:@"满%@元打%0.1f折",[dicShare objectForKey:@"availablePrice"],[[dicShare objectForKey:@"discountPercent"] floatValue]];
        
    }else if (couponType.intValue == 8) {
        str = [NSString stringWithFormat:@"%@元代%@元", [dicShare objectForKey:@"payPrice"], [dicShare objectForKey:@"insteadPrice"]];
        
    }else{
        str = [NSString stringWithFormat:@"%@",[dicShare objectForKey:@"function"]];
        
    }

    NSString *city   = [dicShare objectForKey:@"city"];
    NSString *title = [NSString stringWithFormat:@"【 %@ 】我分享你一张优惠券，手快有，手慢无",city];
//    //    我分享你一张诺亚方舟电影院的优惠券，到惠圈，惠生活！
    NSString *shopName = [NSString stringWithFormat:@"%@", [dicShare objectForKey:@"shopName"]];
   
    NSString* remark = [NSString stringWithFormat:@"%@我分享你一张%@的优惠券，到惠圈，惠生活！",[gloabFunction changeNullToBlank:str],shopName];

    NSString* url = [NSString stringWithFormat:@"%@/BatchCoupon/share?batchCouponCode=%@",BASE_URL,[dicShare objectForKey:@"batchCouponCode"]];
    
    
    [HQShareUtils shareCouponWithTitle:title content:remark url:url];
    

    
}



@end
