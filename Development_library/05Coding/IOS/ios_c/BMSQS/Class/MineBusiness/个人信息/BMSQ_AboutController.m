//
//  BMSQ_AboutController.m
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_AboutController.h"
#import "BMSQ_AboutCell.h"
#import "RRC_webViewController.h"


@interface BMSQ_AboutController ()
{
    UITableView* m_tableView;
    
}
@end

@implementation BMSQ_AboutController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
}



- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavTitle:@"关于"];
    [self setNavBackItem];
    
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 15, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 3;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 44;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString* cellIdentify = @"cellIdentify";
    BMSQ_AboutCell* cell = (BMSQ_AboutCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
    if (cell == nil)
    {
        cell = [[BMSQ_AboutCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    cell.textLabel.font = [UIFont systemFontOfSize:14];
    
    if (indexPath.row == 0)
    {
        [cell setCellValue:@"iv_HQphone" title:@"服务电话: 400-04-95588"];
    }
    else if (indexPath.row == 1)
    {
        [cell setCellValue:@"iv_wxPublic" title:@"微信公众号: huiquanvip"];
    }
    else
    {
        [cell setCellValue:@"iv_aboutHQ" title:@"关于惠圈"];
    }
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.row == 0)
    {
        
        NSURL *phoneNumberURL = [NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",@"4000495588"]];
        [[UIApplication sharedApplication] openURL:phoneNumberURL];
    }
    
    else if (indexPath.row == 2)
    {
        RRC_webViewController* webCtrl = [[RRC_webViewController alloc]init];
        webCtrl.navtitle = @"关于惠圈";
        webCtrl.requestUrl = [NSString stringWithFormat:@"%@/Browser/cAbout",H5_URL];
        [self.navigationController pushViewController:webCtrl animated:YES];
    }
}

@end
