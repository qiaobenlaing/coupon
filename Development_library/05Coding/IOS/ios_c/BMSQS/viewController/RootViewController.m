//
//  RootViewController.m
//  BMSQC
//
//  Created by liuqin on 16/2/22.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "RootViewController.h"
#import "AFJSONRPCClient.h"
#import "UIImageView+WebCache.h"

@interface RootViewController ()

@property (nonatomic, strong)UIImageView *urlImage;

@property (nonatomic, strong)UIActivityIndicatorView *activityView;

@end


@implementation RootViewController


-(void)viewWillAppear:(BOOL)animated{
    
    [super viewWillAppear:animated];
    


}

-(void)viewDidLoad{
    
    self.view.backgroundColor = [UIColor redColor];
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    [imageView setImage:[UIImage imageNamed:@"launchImgeText"]];
    [self.view addSubview:imageView];
    
    self.urlImage= [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    self.urlImage.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.urlImage];
    
    self.activityView =[[UIActivityIndicatorView alloc]initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    self.activityView.center = CGPointMake(APP_VIEW_WIDTH/2, self.urlImage.frame.size.height/2);
    [self.view addSubview:self.activityView];
    [self.activityView startAnimating];
    [NSTimer scheduledTimerWithTimeInterval:1.0f target:self selector:@selector(closeSelf) userInfo:nil repeats:NO];
    [self getGuideInfo];
}

-(void)getGuideInfo{
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:@"1" forKey:@"appType"]; //0B 1C

    __weak typeof(self) weakSelf = self;
    AFJSONRPCClient *jsoncline = [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_COMM_URL]];
    [jsoncline invokeMethod:@"getGuideInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *value = [responseObject objectForKey:@"value"];
        NSString *urlStr = [NSString stringWithFormat:@"%@%@",IMAGE_URL,value];
        [weakSelf.urlImage sd_setImageWithURL:[NSURL URLWithString:urlStr]];
        [weakSelf.activityView stopAnimating];
        
    
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"asdfsad");
    }];

    
    
    
}

- (void)closeSelf
{

    if (self.loadingViewControllerFinish) {
        self.loadingViewControllerFinish();
    }
}
@end
