//
//  BMSQ_modifyPayPwdViewController.m
//  BMSQS
//
//  Created by gh on 16/3/29.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_modifyPayPwdViewController.h"

#import "SetMyPayPasswordController.h"
#import "BMSQ_modifyPayPwd1VIewController.h"

@interface BMSQ_modifyPayPwdViewController ()<UITableViewDataSource,UITableViewDelegate>

@end



@implementation BMSQ_modifyPayPwdViewController


-(void)viewDidLoad{
    
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"支付管理"];
    
    UITableView *taleView =[[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    taleView.separatorStyle = UITableViewCellSeparatorStyleNone;
    taleView.backgroundColor = [UIColor clearColor];
    taleView.delegate = self;
    taleView.dataSource = self;
    [self.view addSubview:taleView];
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 10;
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 2;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSString *identifier = @"modifyPayPwdCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.font = [UIFont systemFontOfSize:13.f];
        cell.textLabel.textColor = APP_TEXTCOLOR;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        
    }
    
    if (indexPath.section ==0) {
        cell.textLabel.text = @"修改支付密码";
    }else if(indexPath.section ==1){
        cell.textLabel.text = @"忘记支付密码";
        
    }
    return cell;
    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        BMSQ_modifyPayPwd1VIewController *vc = [[BMSQ_modifyPayPwd1VIewController alloc]init];
        [self.navigationController pushViewController:vc animated:YES];
        
        
    }else if (indexPath.section==1){
        SetMyPayPasswordController *vc = [[SetMyPayPasswordController alloc]init];
        vc.myTitle = @"忘记支付密码";
        [self.navigationController pushViewController:vc animated:YES];
    }
}
@end
