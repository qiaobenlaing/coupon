//
//  BMSQ_ActvityDetailViewController.m
//  BMSQC
//
//  Created by liuqin on 16/1/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_ActvityDetailViewController.h"

#import "Benefit_activityCell.h"
#import "BMSQ_CouponNbrCell.h"
#import "BMSQ_Share.h"
#import "BMSQ_ActiRefundViewController.h"
#import "MobClick.h"
@interface BMSQ_ActvityDetailViewController ()<UITableViewDelegate,UITableViewDataSource,BMSQ_CouponNbrCellDelegate>

@property (nonatomic, strong)UITableView *actDetailTableView;
@property (nonatomic, strong)NSMutableArray *SeleArray;

@property (nonatomic, strong)NSMutableArray *userActCodeList;
@property (nonatomic, strong)NSDictionary *shareArr;
@property (nonatomic, strong)NSDictionary *activityInfo;



@end


@implementation BMSQ_ActvityDetailViewController



- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"ActvityDetail"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"ActvityDetail"];
}


-(void)viewDidLoad{
    
    [super viewDidLoad];
    
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"我的活动"];
    
    
    self.userActCodeList = [[NSMutableArray alloc]init];
    self.SeleArray = [[NSMutableArray alloc]init];
    
    
    self.actDetailTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.actDetailTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.actDetailTableView.delegate = self;
    self.actDetailTableView.dataSource = self;
    self.actDetailTableView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.actDetailTableView];
    [self getUserActivityInfo];
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 3;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section ==0) {
        return 1;
    }else if (section ==1){
        return self.userActCodeList.count;
    }else{
        return 1;
    }
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        return 140;
    }else if (indexPath.section ==1){
        NSString *str = [self.SeleArray objectAtIndex:indexPath.row];
        if ([str isEqualToString:@"0"])
            return 70;
        else
            return 140;
    }else{
        return 35;
    }
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    if (section ==1) {
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 30)];
        label.backgroundColor = [UIColor clearColor];
        label.text = @"  预付作证";
        label.textColor = APP_TEXTCOLOR;
        label.font = [UIFont systemFontOfSize:12];
        return label;
        
    }else{
        
        return nil;
    }
    
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section ==1) {
        return 30;
        
    }else{
        
        return 0;
    }
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==0) {
        static NSString *actIdentifier = @"actIdentifier";
        Benefit_activityCell *cell = [tableView dequeueReusableCellWithIdentifier:actIdentifier];
        if (cell ==nil) {
            cell = [[Benefit_activityCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:actIdentifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        
        [cell initActivityCell:self.activityInfo];
        return cell;
    }
    else if (indexPath.section ==1){
        static NSString *identifi = @"couponDeatil1";
        BMSQ_CouponNbrCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[BMSQ_CouponNbrCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            cell.NbrDelegate = self;
            cell.isAct = YES;
            
        }
        
        cell.indexpath = (int)indexPath.row;
        NSString *str = [self.SeleArray objectAtIndex:indexPath.row];
        [cell creatNbrCell:[self.userActCodeList objectAtIndex:indexPath.row] isShow:[str isEqualToString:@"1"]?YES:NO];
        return cell;
        
    }else{
        static NSString *actIdentifier = @"actLastCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:actIdentifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:actIdentifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.textLabel.text = @"分享给好友";
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            cell.textLabel.textColor = APP_TEXTCOLOR;
            cell.textLabel.font = [UIFont systemFontOfSize:13.f];
        }
        return cell;
        
    }
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    if (indexPath.section ==0) {
        
        
        
        
        
    }
    else if (indexPath.section ==1){
      
        
        
        
        
        
    }else{
      
    [BMSQ_Share shareClick:[self.shareArr objectForKey:@"content"] imagePath:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.shareArr objectForKey:@"icon"]] title:[self.shareArr objectForKey:@"title"] url:[self.shareArr objectForKey:@"link"]];
        
    }

    
    
}

-(void)applicationCoupon:(NSDictionary *)dic{

    BMSQ_ActiRefundViewController *vc = [[BMSQ_ActiRefundViewController alloc]init];
    vc.actCode = [NSString stringWithFormat:@"%@",[dic objectForKey:@"actCode"]];
    [self.navigationController pushViewController:vc animated:YES];
    
    
}
-(void)clickCouponType:(int)i{
    
    NSString *str = [self.SeleArray objectAtIndex:i];
    if ([str isEqualToString:@"0"]) {
        [self.SeleArray replaceObjectAtIndex:i withObject:@"1"];
    }else{
        [self.SeleArray replaceObjectAtIndex:i withObject:@"0"];
    }
    
    [self.actDetailTableView reloadData];
}

-(void)getUserActivityInfo{
    
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.userActivityCode forKey:@"userActivityCode"];
    
    
    NSString* vcode = [gloabFunction getSign:@"getUserActivityInfo" strParams:self.userActivityCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getUserActivityInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSArray *array = [responseObject objectForKey:@"userActCodeList"];
        [weakSelf.userActCodeList addObjectsFromArray:array];
        weakSelf.shareArr =[responseObject objectForKey:@"shareArr"];
        weakSelf.activityInfo = [responseObject objectForKey:@"activityInfo"];
        for (int i = 0; i<weakSelf.userActCodeList.count; i++) {
            [weakSelf.SeleArray addObject:@"0"];
        }
        [weakSelf.actDetailTableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
    }];
    
    
}

@end
