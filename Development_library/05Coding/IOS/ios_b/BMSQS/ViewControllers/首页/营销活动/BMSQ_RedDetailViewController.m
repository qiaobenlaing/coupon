//
//  BMSQ_ActivityDetailViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_RedDetailViewController.h"
#import "UIImageView+WebCache.h"

@interface BMSQ_RedDetailViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic ,strong) UITableView* tableView;
@property (nonatomic ,strong) NSDictionary *dataDic;
@property (nonatomic ,strong) UIButton* btnSetting ;

@end

@implementation BMSQ_RedDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"红包详情"];
    [self setNavBackItem];
    
    _btnSetting = [UIButton buttonWithType:UIButtonTypeCustom];
    _btnSetting.frame = CGRectMake(APP_VIEW_WIDTH - 50, APP_STATUSBAR_HEIGHT, 44, 44);
    [_btnSetting addTarget:self action:@selector(btnSettingClick) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:_btnSetting];
}


- (void)viewWillLayoutSubviews{
    [super viewWillLayoutSubviews];
    
    _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, self.view.frame.size.width, APP_VIEW_CAN_USE_HEIGHT+44)];
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    
    UIView *footerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, _tableView.frame.size.width, 10)];
    _tableView.tableFooterView = footerView;
    [self.view addSubview:_tableView];
    
    [self getBonusInfo];
    
}

- (void)getBonusInfo{
    
    if (!_activityCode) {
        return;
    }
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString* vcode = [gloabFunction getSign:@"getBonusInfo" strParams:_activityCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:_activityCode forKey:@"bonusCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __block typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getBonusInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        weakSelf.dataDic = responseObject;
        
        NSString *status = responseObject[@"status"];
        if ([status isEqualToString:@"0"]) {
            
            [weakSelf.btnSetting setTitle:@"启用" forState:UIControlStateNormal];
            
        }else if ([status isEqualToString:@"1"]){
            
            [weakSelf.btnSetting setTitle:@"停发" forState:UIControlStateNormal];
        }
        
        [weakSelf.tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];

}


- (void)changeBonusStatus:(NSString *)status bonusCode:(NSString *)bonusCode{
    
    if (!status) {
        return;
    }
    if (!bonusCode) {
        return;
    }
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString* vcode = [gloabFunction getSign:@"changeBonusStatus" strParams:bonusCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:bonusCode forKey:@"bonusCode"];
    [params setObject:status forKey:@"status"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __block typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"changeBonusStatus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        
        /*
         50000-成功；
         20000-失败，请重试；
        */
        switch (resCode) {
            case 50000:{
                NSString  *status = responseObject[@"status"];
                if ([status isEqualToString:@"0"]) {
                    
                    [weakSelf.btnSetting setTitle:@"启用" forState:UIControlStateNormal];
                    
                }else if ([status isEqualToString:@"1"]){
                    
                    [weakSelf.btnSetting setTitle:@"停发" forState:UIControlStateNormal];
                }
                
                [weakSelf getBonusInfo];
                
            }
                break;
            case 20000:
                CSAlert(@"失败，请重试");
                break;
            default:
                break;
        }
      
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         CSAlert(@"失败，请重试");
    }];
    
}


- (void)btnSettingClick{
    
    NSString *status = self.dataDic[@"status"];
    if ([status isEqualToString:@"1"]) {
        
        [self changeBonusStatus:@"0" bonusCode:_activityCode];
        
    }else if ([status isEqualToString:@"0"]){
        
        [self changeBonusStatus:@"1" bonusCode:_activityCode];
    }
    
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

        if (section == 0) {
            return 6;
        }
        if (section == 1) {
            return 2;
        }
        return 1;
   
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    

    
    if (indexPath.section == 0) {
        
        
        static NSString *cellID = @"reuseIdentifier";
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellID];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.font = [UIFont systemFontOfSize:14.f];
        
        if (indexPath.row == 0) {
            cell.textLabel.text = @"红包批次";
            cell.detailTextLabel.text = self.dataDic[@"batchNbr"];
            
        }else if (indexPath.row == 1){
            cell.textLabel.text = @"红包数量";
            cell.detailTextLabel.text = [NSString stringWithFormat:@"%@个", self.dataDic[@"totalVolume"]];
            
        }else if (indexPath.row == 2){
            cell.textLabel.text = @"金额区间";
            cell.detailTextLabel.text = [NSString stringWithFormat:@"%@-%@元",self.dataDic[@"lowerPrice"],self.dataDic[@"upperPrice"]];
            
        }else if (indexPath.row == 3){
            cell.textLabel.text = @"红包总额";
            cell.detailTextLabel.text = [NSString stringWithFormat:@"%@元",self.dataDic[@"totalValue"]];
            
        }else if (indexPath.row == 4){
            cell.textLabel.text = @"开始时间";
            cell.detailTextLabel.text = self.dataDic[@"startTime"];
            
        }else if (indexPath.row == 5){
            cell.textLabel.text = @"结束时间";
            cell.detailTextLabel.text = self.dataDic[@"endTime"];
            
        }
        return cell;

        
    }else{
        
        static NSString *cellID = @"reuseIdentifier2";
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellID];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, 10, APP_VIEW_WIDTH, 30)];
            titleLabel.tag = 100;
            titleLabel.font = [UIFont systemFontOfSize:14.f];
            [cell.contentView addSubview:titleLabel];
            
            UILabel *proBackLable = [[UILabel alloc]initWithFrame:CGRectMake(15, 40,APP_VIEW_WIDTH/2+50, 10)];
            proBackLable.layer.masksToBounds = YES;
            proBackLable.layer.cornerRadius = 8;
            proBackLable.layer.borderWidth = 0.5;
            proBackLable.layer.borderColor = [[UIColor grayColor]CGColor];
            [cell.contentView addSubview:proBackLable];
            
            UILabel *proBackLable1 = [[UILabel alloc]initWithFrame:CGRectMake(5,2, 0,6)];
            proBackLable1.backgroundColor = UICOLOR(182, 0, 12, 1.0);
            proBackLable1.layer.masksToBounds = YES;
            proBackLable1.layer.cornerRadius = 6;
            proBackLable1.tag = 101;
            [proBackLable addSubview:proBackLable1];
            
            UILabel *proLabel = [[UILabel alloc]initWithFrame:CGRectMake(15 , 50 , APP_VIEW_WIDTH,30)];
            proLabel.font = [UIFont systemFontOfSize:14.f];
            proLabel.backgroundColor = [UIColor clearColor];
            proLabel.tag =  111;
            [cell.contentView addSubview:proLabel];
            
            
            
        }
        UILabel *titleLabel = (UILabel *)[cell.contentView viewWithTag:100];
        UILabel *proLabel = (UILabel *)[cell.contentView viewWithTag:111];
        UILabel *proBackLable = (UILabel *)[cell.contentView viewWithTag:101];

        if (indexPath.row == 0) {
            titleLabel.text = @"领取人数";
            
            if ([self.dataDic objectForKey:@"getNbr"]) {
                float w = [self.dataDic[@"getNbr"]floatValue]/[self.dataDic[@"totalVolume"]floatValue]*((APP_VIEW_WIDTH/2+50)-10);
                proBackLable.frame =CGRectMake(5,2, w,6);
            }
            proLabel.text = [NSString stringWithFormat:@"未领取停发(%@/%@)",self.dataDic[@"getNbr"],self.dataDic[@"totalVolume"]];
        }else if (indexPath.row == 1){
            titleLabel.text = @"领用金额";
            
            if ([self.dataDic objectForKey:@"getValue"]) {
                float w = [self.dataDic[@"getValue"]floatValue]/[self.dataDic[@"totalValue"]floatValue]*(proBackLable.frame.size.width-10);
                proBackLable.frame =CGRectMake(5,2, w,6);
            }

            proLabel.text = [NSString stringWithFormat:@"未领取(%@/%@)",self.dataDic[@"getValue"],self.dataDic[@"totalValue"]];


            
        }
        
        return cell;

        
    }
    

}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    if (section == 0) {
        return 0;
    }
    return 20;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        return 44;

    }else{
        return 80;
    }
}



@end
