//
//  SDZX_cityViewController.m
//  SDBooking
//
//  Created by djx on 14-3-18.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "RRC_cityViewController.h"
#import "AppDelegate.h"
@interface RRC_cityViewController ()

@end

@implementation RRC_cityViewController

@synthesize delegate;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"选择城市"];
    
    RRC_cityListView* cityList = [[RRC_cityListView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    cityList.delegate = self;
    [self.view addSubview:cityList];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    //SDZX_AppDelegate* appDelegate = (SDZX_AppDelegate*)[UIApplication sharedApplication].delegate;
    
    //[appDelegate.tabBarController hidesTabBar:YES animated:NO];
}

//- (void)view:(BOOL)animated
//{
//    [super viewWillDisappear:YES];
//    [appDelegate.tabBarController hidesTabBar:NO animated:NO];
//}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)goBack
{
    [self setMapCity];
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark cityList delegate

- (void)selectCity:(SDZX_cityObject*)cityObj
{

    if (delegate != nil&& [delegate respondsToSelector:@selector(selectCity:)])
    {
        
        [delegate selectCity:cityObj];
    }

    [self goBack];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
