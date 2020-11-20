//
//  BMSQ_AboutViewController.m
//  BMSQS
//
//  Created by lxm on 15/8/8.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_AboutViewController.h"
#import "BMSQ_AgreementViewController.h"

@interface BMSQ_AboutViewController ()

@end

@implementation BMSQ_AboutViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavTitle:@"关于"];
    [self setNavBackItem];
    
    
    UITableView *tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];

    tableView.dataSource = self;
    tableView.delegate = self;
    
    [self.view addSubview:tableView];

}



-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 3;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 44.f;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = @"TableViewCell";
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
    }else{
        
    }
    

    if(indexPath.row==0){
        setCell.imageView.image = [UIImage imageNamed:@"电话服务"];
        setCell.textLabel.text = @"服务电话 400-04-95588";

    }else if(indexPath.row==1){
        setCell.imageView.image = [UIImage imageNamed:@"微信公众号"];
        setCell.textLabel.text = @"微信公众号 huiquanvip";

    }else if(indexPath.row==2){
        setCell.imageView.image = [UIImage imageNamed:@"58"];
        setCell.textLabel.text = @"关于惠圈";
//        setCell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }
//    else if (indexPath.row==3){
//        setCell.imageView.image = [UIImage imageNamed:@"58"];
//        setCell.textLabel.text = @"1.0";
//    }
    setCell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (indexPath.row == 0) {
        NSMutableString * str=[[NSMutableString alloc] initWithFormat:@"telprompt://%@",@"4000495588"];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:str]];
    }
    
    if(indexPath.row==2){
        UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
        BMSQ_AgreementViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Agreement"];
        vc.hidesBottomBarWhenPushed = YES;
        vc.navViewTitle = @"关于惠圈";
        vc.url = [NSString stringWithFormat:@"%@/Browser/sAbout",APP_SERVERCE_H5_URL ];
        [self.tabBarController.tabBar setHidden:YES];
        [self.navigationController pushViewController:vc animated:YES];

    }
}


- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section;
{
    return 100.f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 0.1f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 9)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}


@end
