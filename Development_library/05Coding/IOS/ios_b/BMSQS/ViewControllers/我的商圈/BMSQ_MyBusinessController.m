//
//  BMSQ_MyBusinessController.m
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "SLCGRectHelper.h"
#import "BMSQ_MineShopInfoController.h"
#import "UIImageView+WebCache.h"
#import "BMSQ_StaffListTableViewController.h"
#import "BMSQ_PosServiceViewController.h"
#import "BaseNavViewController.h"
#import "BMSQ_LoginViewController.h"
#import "BMSQ_AboutViewController.h"
#import "BMSQ_ModifyPasswordViewController.h"
#import "BMSQ_ShopOrderViewController.h"
#import "BMSQ_SetUpViewController.h"
#import "DecorationShopViewController.h"
#import "BMSQ_MineShopViewController.h"
#import "BMSQ_MemberChartViewController.h"
#import "BMSQ_BranchViewController.h"
#import "MyBankCardViewController.h"
#import "SatffViewController.h"  //店员管理
#import "BMSQ_SMSSettingViewController.h" //短信设置

#import "BMSQ_MyBusinessCell.h"// 自定义cell

#import "SetMyPayPasswordController.h"// 第一次设置支付密码
#import "BMSQ_modifyPayPwdViewController.h" //

/*---我的订单---*/
#import "BMSQ_mineOrderViewController.h"
#import "BMSQ_AwayOrderViewController.h"

#define APP_FOTERVIEW_HEIGHT 13

@interface HeaderCell : UITableViewCell



@end

@implementation HeaderCell

- (void)layoutSubviews{
    [super layoutSubviews];
    
    self.imageView.frame = SLRectMake(15, 8, 76, 76);
    self.imageView.backgroundColor = [UIColor whiteColor];
    
    CGRect rect = self.textLabel.frame;
    rect.origin = CGPointMake(CGRectGetMaxX(self.imageView.frame) +10, 12);
    rect.size   = CGSizeMake(self.frame.size.width - rect.origin.x -20, 15);
    self.textLabel.frame = rect;
    self.textLabel.font = [UIFont systemFontOfSize:14.0];;
    
    rect = self.detailTextLabel.frame;
    rect.origin = CGPointMake(CGRectGetMaxX(self.imageView.frame) +10, CGRectGetMaxY(self.textLabel.frame) +5);
    rect.size.height = self.frame.size.height - rect.origin.y-5;
    rect.size.width  = CGRectGetWidth(self.textLabel.frame);
    self.detailTextLabel.frame = rect;
    self.detailTextLabel.numberOfLines = 0;
    self.detailTextLabel.font = [UIFont systemFontOfSize:12.0];
    self.detailTextLabel.textColor = [UIColor lightGrayColor];
    self.detailTextLabel.backgroundColor = [UIColor clearColor];

    
}

@end

#import "BMSQ_MyBusinessController.h"

@interface BMSQ_MyBusinessController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic ,strong) UITableView* tableView;
@property (nonatomic ,strong) NSDictionary *shopDataDic;

@property(nonatomic, strong)NSString *userlvs; //1-店员，2-店长，3-大店长

@end

@implementation BMSQ_MyBusinessController



- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavTitle:@"我的商铺"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, self.view.frame.size.width, APP_VIEW_CAN_USE_HEIGHT)];
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [_tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"UITableViewCell"];
    
    [self.view addSubview:_tableView];
    
}


- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    [self sGetShopBasicInfo];
    //1-店员，2-店长，3-大店长
    self.userlvs = [gloabFunction getUserLvl];
}






#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 6;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if(section==1){
        if ([self.userlvs isEqualToString:BOSS] || [self.userlvs isEqualToString:MANAGER])
            return 2;
        else
            return 1;
        

    }
    else if (section == 3)
        return 2;
    else if(section == 2){
         return 2;
    }
    else if (section == 4){
        return 2;
    }
    else if (section==0){
        return 3;
        
    }


    return 0;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    if (indexPath.section == 0 && indexPath.row == 0) {
        static NSString *identitfier = @"HeaderCell";
        __block HeaderCell *cell = (HeaderCell *)[tableView dequeueReusableCellWithIdentifier:identitfier ];
        if (!cell) {
            cell = [[HeaderCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:identitfier];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
//        NSDictionary *shopInfo = [_shopDataDic objectForKey:@"shopInfo"];
        if ( [_shopDataDic isKindOfClass:[NSDictionary class]]) {
            cell.textLabel.text = _shopDataDic[@"shopName"];
            
            if(_shopDataDic[@"street"]){
                cell.detailTextLabel.text = [NSString stringWithFormat:@"地址:%@",_shopDataDic[@"street"]];
                
            }else {
                
                cell.detailTextLabel.text = @"地址：未知";
                
            }
            
            NSString *urlStr = [NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,_shopDataDic[@"logoUrl"]];
            cell.imageView.backgroundColor = [UIColor whiteColor];
            [cell.imageView sd_setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"Login_Icon"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                [cell setNeedsLayout];
                
            }];
            
        }
        
        return cell;
        

    }else{
        
        NSString *identifier = @"BMSQ_MyBusinessCell";
        BMSQ_MyBusinessCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[BMSQ_MyBusinessCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
        }
        if (indexPath.section == 0) {
            if (indexPath.row == 1) {
                [cell setCellValue:@"no_iden_pay" label:@"支付管理"];
            }else if(indexPath.row == 2) {
                [cell setCellValue:@"iv_myICBC" label:@"我的银行卡"];
            }
        }
        
        if (indexPath.section == 1) {
            if (indexPath.row == 0) {
                [cell setCellValue:@"订单管理" label:@"我的订单"];
            }
            
            if ([self.userlvs isEqualToString:BOSS] || [self.userlvs isEqualToString:MANAGER]) {
                if (indexPath.row == 1) {
                    [cell setCellValue:@"staff_manage.jpg" label:@"店员管理"];
                }
            }
        }
        if (indexPath.section == 2) {
            
            if (indexPath.row == 0) {
                [cell setCellValue:@"我的店铺" label:@"我的店铺"];
                
            }
            if (indexPath.row == 1) {
                [cell setCellValue:@"对惠圈的建议" label:@"对惠圈的建议"];

            }
            
            
        }
        else if(indexPath.section == 3){
            
            if (indexPath.row == 0) {
                [cell setCellValue:@"修改密码" label:@"修改密码"];
                
            }else if(indexPath.row == 1){
                [cell setCellValue:@"recevieMSM" label:@"接收短信设置"];
            }
            
        }
        else if(indexPath.section == 4) {
            
            if (indexPath.row == 0) {
                [cell setCellValue:@"关于" label:@"关于"];
            } else if (indexPath.row == 1) {
                [cell setCellValue:@"设置" label:@"设置"];

            }
        }
        
        return cell;
        
    }
    
//    return nil;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0 && indexPath.row == 0) {
        return 80;
    }else{
        return 40;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    if (section == 0) {
        return 0;
    }else if(section == 6){
        return 0;
    }
    return 8;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    
    if (section == 6) {
        return 0;
    }
    return 0;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.section == 0 ) {
        if (indexPath.row == 0) {
            
            BMSQ_MineShopInfoController *pushVC = [[BMSQ_MineShopInfoController alloc] init];
            pushVC.shopInfoDic = _shopDataDic[@"shopInfo"];
            pushVC.shopAllMsg = _shopDataDic;
            pushVC.hidesBottomBarWhenPushed = YES;
            [pushVC.tabBarController.tabBar setHidden:YES];
            [self.navigationController pushViewController:pushVC animated:YES];
            
        }else if (indexPath.row == 1) {//支付管理
            [self ifShopSetPayPwd];
            
            
        }else if (indexPath.row == 2) {//我的银行卡
            MyBankCardViewController *vc = [[MyBankCardViewController alloc] init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
        }
    }
    
    if (indexPath.section == 1) {
        
        if (indexPath.row == 0){ //-订单管理
            
            NSLog(@"----->我的订单");
            NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
            NSString *isFood = [uD objectForKey:SHOPTYPE_FOOD];
            if ([isFood isEqualToString:@"1"]) {   //餐饮
                BMSQ_mineOrderViewController * mineVC = [[BMSQ_mineOrderViewController alloc] init];
                mineVC.hidesBottomBarWhenPushed = YES;
                [self.navigationController pushViewController:mineVC animated:YES];
            }else{
                                                   //非餐饮
                BMSQ_AwayOrderViewController * mineVC = [[BMSQ_AwayOrderViewController alloc] init];
                mineVC.hidesBottomBarWhenPushed = YES;
                [self.navigationController pushViewController:mineVC animated:YES];
                
            }
            
            
    
            
        }
        if ([self.userlvs isEqualToString:BOSS] || [self.userlvs isEqualToString:MANAGER])
        {
            if (indexPath.row == 1) { //员工管理
                SatffViewController *vc = [[SatffViewController alloc]init];
                vc.hidesBottomBarWhenPushed = YES;
                [self.navigationController pushViewController:vc animated:YES];
            }
        }

    }else if(indexPath.section ==2){
        if  (indexPath.row == 0) {//-我的店铺
            BMSQ_MineShopViewController *mineShopVC = [[BMSQ_MineShopViewController alloc] init];
            mineShopVC.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:mineShopVC animated:YES];
            
        }
        if (indexPath.row == 1) { //对惠圈的建议
            BMSQ_MemberChartViewController *vc = [[BMSQ_MemberChartViewController alloc] init];
            vc.userID = APP_HQ_CODE;
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
            
        }
        
        
    } else if (indexPath.section == 3) {
        if (indexPath.row == 0){//修改密码
            UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"DecorationShop" bundle:nil];
            BMSQ_ModifyPasswordViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_ModifyPassword"];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
            
        }else if (indexPath.row == 1){
            BMSQ_SMSSettingViewController *vc = [[BMSQ_SMSSettingViewController alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
        }
    } else if (indexPath.section == 4) {
        if (indexPath.row == 0) {//关于
            BMSQ_AboutViewController *pushVC = [[BMSQ_AboutViewController alloc] init];
            pushVC.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:pushVC animated:YES];
        }
        if (indexPath.row == 1) {//设置
            BMSQ_SetUpViewController *setUpVC = [[BMSQ_SetUpViewController alloc] init];
            setUpVC.mobileNbr = [NSString stringWithFormat:@"%@",_shopDataDic[@"shopInfo"][@"mobileNbr"]];
            setUpVC.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:setUpVC animated:YES];
            
        }
       
       
    } else if (indexPath.section == 6) { //接收短信设置页面
        
//        BMSQ_AboutViewController *pushVC = [[BMSQ_AboutViewController alloc] init];
//        pushVC.hidesBottomBarWhenPushed = YES;
//        [self.navigationController pushViewController:pushVC animated:YES];
        
      
    }

}

#pragma  mark - request
- (void)sGetShopBasicInfo{
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"sGetShopBasicInfo" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"sGetShopBasicInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        
        weakSelf.shopDataDic = responseObject;
        [weakSelf.tableView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        
    }];
}

//商家是否设置了支付密码
- (void)ifShopSetPayPwd {
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@"" maskType:SVProgressHUDMaskTypeClear];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"ifShopSetPayPwd" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"ifShopSetPayPwd" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        NSString *str = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        
        if ([str isEqualToString:@"1"]) {
            BMSQ_modifyPayPwdViewController *vc = [[BMSQ_modifyPayPwdViewController alloc]init];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
        }else{
            SetMyPayPasswordController *vc = [[SetMyPayPasswordController alloc]init];
            vc.myTitle = @"设置支付密码";
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];
//            BMSQ_setMyCardViewController *vc = [[BMSQ_setMyCardViewController alloc]init];
//            vc.formvc = 1;
//            vc.myTitle = @"设置支付密码";
//            vc.hidesBottomBarWhenPushed = YES;
//            [self.navigationController pushViewController:vc animated:YES];
        }

        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
}


@end
