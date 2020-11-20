//
//  BMSQ_CountViewController.m
//  BMSQS
//
//  Created by liuqin on 15/11/23.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CountViewController.h"
#import "SVProgressHUD.h"


 #import "UUChart.h"

@interface BMSQ_CountViewController () <UUChartDataSource> {

    UUChart *countCharView;
    NSMutableArray *xValues;   //这个是横坐标的内容
    NSMutableArray *yValues;  //这个是竖坐标的内容（高压）
    
    int maxValue;
    int minValue;
    
    
}

@property (strong, nonatomic) UILabel *todCouLabel;

@end

@implementation BMSQ_CountViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavTitle:@"用户统计"];
    
    self.todCouLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 40, 110, 20)];
    self.todCouLabel.text =@"0";
    self.todCouLabel.font = [UIFont systemFontOfSize:13];
    self.todCouLabel.textColor = APP_TEXTCOLOR;
    
    [self getShopTodayBrowseQuantity];
    
    UIView *topView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 99)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:topView];
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 75, APP_VIEW_WIDTH, 10)];
    label.text = @"总浏览量";
    label.textAlignment = NSTextAlignmentCenter;
    label.font = [UIFont systemFontOfSize:12];
    label.textColor = APP_TEXTCOLOR;
    [topView addSubview:label];
    
    
    UIImageView *iconimageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 15, 20, 18)];
    [iconimageView setImage:[UIImage imageNamed:@"manager.png"]];
    [topView addSubview:iconimageView];
    iconimageView.center = CGPointMake(APP_VIEW_WIDTH/2, 15);
    
     UILabel *countLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, 50)];
    countLabel.textColor = APP_TEXTCOLOR;
    countLabel.font = [UIFont systemFontOfSize:20];
    countLabel.text = self.count;
    countLabel.textAlignment = NSTextAlignmentCenter;
    [topView addSubview:countLabel];


    [self getShopDayBrowseQuantity];

}

- (void)addLineView {
    
    UIView *secView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 100, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-100)];
    secView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:secView];
    
    
    UILabel *todayLable = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 100, 40)];
    todayLable.text =@"今日访问量";
    todayLable.textAlignment = NSTextAlignmentCenter;
    todayLable.textColor = APP_TEXTCOLOR;
    [secView addSubview:todayLable];
    
    
    [secView addSubview:self.todCouLabel];
    
    UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(10, 60, APP_VIEW_WIDTH-20, 1)];
    lineView.backgroundColor = self.view.backgroundColor;
    [secView addSubview:lineView];
    
    xValues = [[NSMutableArray alloc] init];
    yValues = [[NSMutableArray alloc] init];
    
    int i = 0;
    for (NSDictionary *dic in self.countArray) {
        
        NSString *str = [NSString stringWithFormat:@"%@",[dic objectForKey:@"day"]];
        
        str = [str substringFromIndex:5];
        if (i%2 == 0) {
            [xValues addObject:str];
        }else {
            [xValues addObject:@""];
        }
        
        
        
        str = [dic objectForKey:@"count"];
        [yValues addObject:str];
        
        maxValue = maxValue >[[NSString stringWithFormat:@"%@",[dic objectForKey:@"count"]]intValue]?maxValue:[[NSString stringWithFormat:@"%@",[dic objectForKey:@"count"]]intValue];
        minValue = minValue <[[NSString stringWithFormat:@"%@",[dic objectForKey:@"count"]]intValue]?minValue:[[NSString stringWithFormat:@"%@",[dic objectForKey:@"count"]]intValue];
        
        i++;
        
    }
    maxValue = maxValue + maxValue/2;
    
    
    countCharView = [[UUChart alloc]initwithUUChartDataFrame:CGRectMake(10, 70, APP_VIEW_WIDTH -20, 150)
                                                  withSource:self
                                                   withStyle:UUChartLineStyle];
    
    [secView addSubview:countCharView];
    [countCharView showInView:secView];
    
}



-(void)getShopTodayBrowseQuantity{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopTodayBrowseQuantity" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:@"0" forKey:@"page"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getShopTodayBrowseQuantity" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];

        NSString *str = responseObject;
        weakSelf.todCouLabel.text = str;
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];

        
    }];
    
    
}

#pragma mark - UUCharView Delegate

//该方法是返回实例化的折线图的横坐标

-(NSArray *)UUChart_xLableArray:(UUChart *)chart

{
    
    return xValues;
    
    
    
}

//该方法是返回实例化的折线图的竖坐标（若返回一个就是一条折线图，我这里需要显示高压低压，所以返回了两个）

-(NSArray *)UUChart_yValueArray:(UUChart *)chart

{
    
    return @[yValues];
    
}

//这里返回纵坐标的范围。

-(CGRange)UUChartChooseRangeInLineChart:(UUChart *)chart

{
    
    return CGRangeMake(maxValue,minValue);
    
}


//颜色数组

- (NSArray *)UUChart_ColorArray:(UUChart *)chart

{
    
    return @[UUGreen,UURed,UUBrown];
    
}

//判断显示横线条
- (BOOL)UUChart:(UUChart *)chart ShowHorizonLineAtIndex:(NSInteger)index

{
    
    return YES;
    
}


// 最近浏览量
-(void)getShopDayBrowseQuantity{
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopDayBrowseQuantity" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:@"0" forKey:@"page"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopDayBrowseQuantity" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        weakSelf.countArray = responseObject;
        [self addLineView];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
}



@end
