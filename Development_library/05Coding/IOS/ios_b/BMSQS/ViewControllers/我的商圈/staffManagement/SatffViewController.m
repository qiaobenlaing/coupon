//
//  SatffViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "SatffViewController.h"
#import "BaomiViewUtils.h"
#import "AddStatffViewController.h"
#import "BMSQ_BranchViewController.h"
#import "EditStatffViewController.h"


@interface SatffViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong)UITableView *staffTableView;

@property (nonatomic, strong)NSMutableArray *staffArray;

@property (nonatomic, strong)NSMutableArray *seleArray;

@property (nonatomic, strong)NSMutableDictionary *bManager;

@property (nonatomic, strong)NSArray *resultArray;

@property (nonatomic, strong)NSString *userLvs;

@end

@implementation SatffViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    
    [self setNavTitle:@"店员管理"];
    [self setNavBackItem];
    

    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(editStaff) name:@"editStaff" object:nil];
    
    self.userLvs = [gloabFunction getUserLvl];
    
    
    self.bManager = [[NSMutableDictionary alloc]init];
    self.staffArray = [[NSMutableArray alloc]init];
    self.seleArray = [[NSMutableArray alloc]init];
    
    
    self.staffTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-60)];
    self.staffTableView.backgroundColor = [UIColor clearColor];
    self.staffTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.staffTableView.delegate = self;
    self.staffTableView.dataSource = self;
    [self.view addSubview:self.staffTableView];
    [self getStaffShopList];
    

}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    if (indexPath.section ==2) {
        static NSString *idenfitier = @"staffCellButton";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:idenfitier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idenfitier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UIButton *addButton = [BaomiViewUtils creatBtn:CGRectMake(15, 50, APP_VIEW_WIDTH-30, 35) title:@"添加店长" titleColor:[UIColor whiteColor] bgColor:UICOLOR(182, 0, 12, 1.0) tag:100];
            addButton.layer.cornerRadius = 4;
            addButton.layer.masksToBounds = YES;
            addButton.titleLabel.font = [UIFont systemFontOfSize:14.f];
            addButton.tag = 100;
            [cell.contentView addSubview:addButton];
            
            UIButton *manageButton = [BaomiViewUtils creatBtn:CGRectMake(15, 100, APP_VIEW_WIDTH-30, 35) title:@"店员管理" titleColor:[UIColor whiteColor] bgColor:UICOLOR(182, 0, 12, 1.0) tag:200];
            manageButton.layer.cornerRadius = 4;
            manageButton.layer.masksToBounds = YES;
            manageButton.titleLabel.font = [UIFont systemFontOfSize:14.f];
            manageButton.tag = 200;
            [cell.contentView addSubview:manageButton];
            
            [manageButton addTarget:self action:@selector(gotoManagement) forControlEvents:UIControlEventTouchUpInside];
            [addButton addTarget:self action:@selector(gotoAddManagement) forControlEvents:UIControlEventTouchUpInside];
            manageButton.hidden = YES;
            addButton.hidden = YES;

        }
        
        UIButton *addButton = (UIButton *)[cell.contentView viewWithTag:100];
        UIButton *manageButton = (UIButton *)[cell.contentView viewWithTag:200];

        if ([self.userLvs isEqualToString:BOSS]) {
            addButton.hidden = NO;
            manageButton.hidden = NO;
        }else if ([self.userLvs isEqualToString:MANAGER]){
            addButton.hidden = YES;
            manageButton.hidden = NO;
        }else{
            addButton.hidden = YES;
            manageButton.hidden = YES;

        }
        
        
          return cell;
    }
    else if(indexPath.section ==0){
        static NSString *idenfitier = @"staffCellButton_0";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:idenfitier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idenfitier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            
            UILabel *shopLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 43)];
            shopLabel.backgroundColor = [UIColor whiteColor];
            shopLabel.font = [UIFont systemFontOfSize:14.f];
            shopLabel.tag = 100;
            [cell addSubview:shopLabel];
        }
        
        UILabel *label = (UILabel *)[cell viewWithTag:100];
        if (self.bManager.count >0) {
             label.text = [NSString stringWithFormat:@"  %@:%@",[self.bManager objectForKey:@"realName"],[self.bManager objectForKey:@"mobileNbr"]];
        }
        
       
        
        return cell;
    }
    else if(indexPath.section ==1){
        static NSString *idenfitier = @"staffCellButton_1";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:idenfitier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idenfitier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            
            
            
            UILabel *shopLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 44)];
            shopLabel.textColor = [UIColor blackColor];
            shopLabel.tag = 100;
            shopLabel.font = [UIFont systemFontOfSize:14.f];
            shopLabel.backgroundColor = [UIColor clearColor];
            [cell addSubview:shopLabel];
            
            
//            UIButton *shoperButton = [BaomiViewUtils creatBtn:CGRectMake(0, 0, APP_VIEW_WIDTH, 44) title:@"" titleColor:[UIColor clearColor] bgColor:[UIColor clearColor] cornerRadius:0 borderColor:[UIColor clearColor] borderWidth:0 tag:99];
////            shoperButton.titleLabel.font = [UIFont systemFontOfSize:15.f];
//            [shoperButton addTarget:self action:@selector(clickTitle:) forControlEvents:UIControlEventTouchUpInside];
//            [cell addSubview:shoperButton];

            
            
            UIView *secView = [[UIView alloc]init];
            secView.tag =  200;
            [cell addSubview:secView];
            secView.backgroundColor = [UIColor clearColor];
            secView.userInteractionEnabled = YES;
            
            
            UIView *buttonBgView = [[UIView alloc]init];
            buttonBgView.backgroundColor = [UIColor whiteColor];
            buttonBgView.tag = 222;
            [cell addSubview:buttonBgView];
            buttonBgView.hidden = YES;
            
            UIButton *editButton = [BaomiViewUtils creatBtn:CGRectZero title:@"编辑店长" titleColor:[UIColor blackColor] bgColor:[UIColor whiteColor] cornerRadius:5.0 borderColor:UICOLOR(182, 0, 12, 1.0) borderWidth:0.8 tag:10];
            editButton.titleLabel.font = [UIFont systemFontOfSize:13.f];
            [editButton addTarget:self action:@selector(editStaff:) forControlEvents:UIControlEventTouchUpInside];
            [buttonBgView addSubview:editButton];
            editButton.hidden = YES;
            
            
            UIButton *setButton = [BaomiViewUtils creatBtn:CGRectZero title:@"设置门店" titleColor:[UIColor blackColor] bgColor:[UIColor whiteColor] cornerRadius:5.0 borderColor:UICOLOR(182, 0, 12, 1.0) borderWidth:0.8 tag:20];
            setButton.titleLabel.font = [UIFont systemFontOfSize:13.f];
            [setButton addTarget:self action:@selector(editOwner:) forControlEvents:UIControlEventTouchUpInside];

            [buttonBgView addSubview:setButton];
            setButton.hidden = YES;
            
        }
        UILabel *shoperlabel = (UILabel *)[cell viewWithTag:100];

//        UIButton *shoperButton = (UIButton *)[cell viewWithTag:99];
//        shoperButton.tag = 1000+indexPath.row;

        UIView *secView = (UIView *)[cell viewWithTag:200];
        UIView *butBgView = (UIView *)[cell viewWithTag:222];

        UIButton *editButton = (UIButton *)[cell viewWithTag:10];
        UIButton *setButton = (UIButton *)[cell viewWithTag:20];
        
        NSDictionary *dic = [self.staffArray objectAtIndex:indexPath.row];
        NSString *shoperstr = [NSString stringWithFormat:@"  %@:%@",[dic objectForKey:@"realName"],[dic objectForKey:@"mobileNbr"]];
        
        shoperlabel.text = shoperstr;

        for (UIView *abcView in secView.subviews) {
            [abcView removeFromSuperview];
        }
        
        
        NSArray *shopList = [dic objectForKey:@"shopList"];
        for (int i =0 ;i<shopList.count;i++) {
            NSDictionary *dic = [shopList objectAtIndex:i];
            UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, i*44, APP_VIEW_WIDTH, 43)];
            label.text =[NSString stringWithFormat:@"  %@",[dic objectForKey:@"shopName"]] ;
            label.font = [UIFont systemFontOfSize:13.f];
            label.backgroundColor = [UIColor whiteColor];
            [secView addSubview:label];
        }
        secView.frame = CGRectMake(0, 44, APP_VIEW_WIDTH, 44*shopList.count);
        
        if ([self.userLvs isEqualToString:MANAGER]) {
            butBgView.frame = CGRectMake(0, secView.frame.origin.y+secView.frame.size.height+1, APP_VIEW_WIDTH, 0);
            editButton.frame = CGRectMake(APP_VIEW_WIDTH-200,10, 0, 0);
            setButton.frame = CGRectMake(APP_VIEW_WIDTH-200+100, editButton.frame.origin.y, 0, 0);
        }else{
            butBgView.frame = CGRectMake(0, secView.frame.origin.y+secView.frame.size.height+1, APP_VIEW_WIDTH, 43);
            editButton.frame = CGRectMake(APP_VIEW_WIDTH-200,10, 80, 28);
            setButton.frame = CGRectMake(APP_VIEW_WIDTH-200+100, editButton.frame.origin.y, 80, 28);
        }
        
        
        NSString *isshow = [self.seleArray objectAtIndex:indexPath.row];
        if ([isshow isEqualToString:@"1"]) {
            secView.hidden = NO;
            editButton.hidden = NO;
            setButton.hidden = NO;
            butBgView.hidden = NO;
            
        }else{
            secView.hidden = YES;
            editButton.hidden = YES;
            setButton.hidden = YES;
            butBgView.hidden = YES;

        }
        
        return cell;
    }

    
    static NSString *idenfitier = @"staffCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:idenfitier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idenfitier];
    
    }
    
    return cell;
    
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section ==0) {
        return 44;
    }else if (section ==1){
        return 44;
    }else{
        return 0;
    }
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    if (section ==0) {
        return 1;
    }else if (section ==1){
        return self.staffArray.count;
    }else if (section ==2){
        return 1;
    }
    
    return 10;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        return  50;
    }else if(indexPath.section ==1){
        
        NSString *isShow = [self.seleArray objectAtIndex:indexPath.row];
        
        if ([isShow isEqualToString:@"1"]) {
            NSDictionary *dic = [self.staffArray objectAtIndex:indexPath.row];
            NSArray *shopList = [dic objectForKey:@"shopList"];
            return 44+44*shopList.count + 44+5;
            
        }else{
            return 44;
        }
        
    }else if (indexPath.section ==2){
        return 200;
    }
    
    return 40;
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView *headView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 44)];
    headView.backgroundColor = [UIColor clearColor];
    
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 43)];
    label.backgroundColor = [UIColor whiteColor];
    label.font = [UIFont systemFontOfSize:16.f];
    if (section ==0) {
        label.text = @"  大店长";
    }else{
        label.text = @"  店长";
    }
    [headView addSubview:label];

    
    return headView;
    
    
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 3;
}

-(void)gotoManagement{
    BMSQ_BranchViewController *vc = [[BMSQ_BranchViewController alloc] init];
    [self.navigationController pushViewController:vc animated:YES];
    
}

#pragma mark -- 添加店长--
-(void)gotoAddManagement{
    AddStatffViewController *vc = [[AddStatffViewController alloc]init];
    vc.brandId = [self.bManager objectForKey:@"brandId"];
    vc.navTitleStr = @"添加店长";
    [self.navigationController pushViewController:vc animated:YES];
}



-(void)getStaffShopList{
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getStaffCode] forKey:@"staffCode"];
    NSString* vcode = [gloabFunction getSign:@"getManAdmin" strParams:[gloabFunction getStaffCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __block typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"getManAdmin" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        
        if ([responseObject objectForKey:@"bManager"]) {
            weakSelf.bManager = [[responseObject objectForKey:@"bManager"]mutableCopy];

        }
        
        if([responseObject objectForKey:@"manager"]){
            NSArray *managers = [responseObject objectForKey:@"manager"];
            
            [weakSelf.staffArray addObjectsFromArray:managers];

        }
        
        [weakSelf.seleArray removeAllObjects];
        
        for (NSDictionary *dic in weakSelf.staffArray) {
            NSLog(@"%@",dic);
            [weakSelf.seleArray addObject:@"1"];
        }

        [weakSelf.staffTableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}

-(void)editStaff:(UIButton *)button{
    
    UIView *buttonSuperView = button.superview;
   UITableViewCell *cell =(UITableViewCell *) buttonSuperView.superview;
   NSIndexPath *indexPath = [self.staffTableView indexPathForCell:cell];
   int i = (int)indexPath.row;
    NSDictionary *dic = [self.staffArray objectAtIndex:i];
    
    AddStatffViewController *vc = [[AddStatffViewController alloc]init];
    vc.realName = [dic objectForKey:@"realName"];
    vc.mobileNbr = [dic objectForKey:@"mobileNbr"];
    vc.staffCode = [dic objectForKey:@"staffCode"];
    vc.userLvl = [NSString stringWithFormat:@"%@",[dic objectForKey:@"userLvl"]];
    vc.brandId = [NSString stringWithFormat:@"%@",[dic objectForKey:@"brandId"]];
    vc.navTitleStr = @"编辑店长";
    [self.navigationController pushViewController:vc animated:YES];
    

}

-(void)editOwner:(UIButton *)button{
    
    UIView *buttonSuperView = button.superview;
    UITableViewCell *cell =(UITableViewCell *) buttonSuperView.superview;
    NSIndexPath *indexPath = [self.staffTableView indexPathForCell:cell];
    int i = (int)indexPath.row;
    NSDictionary *dic = [self.staffArray objectAtIndex:i];
    
    EditStatffViewController *vc = [[EditStatffViewController alloc]init];
//    vc.realName = [dic objectForKey:@"realName"];
//    vc.mobileNbr = [dic objectForKey:@"mobileNbr"];
    vc.staffCode = [dic objectForKey:@"staffCode"];
//    vc.userLvl = [dic objectForKey:@"userLvl"];
    [self.navigationController pushViewController:vc animated:YES];
}

-(void)editStaff{
    
    
    [self.bManager removeAllObjects];
    [self.staffArray removeAllObjects];
    [self.seleArray removeAllObjects];
    
    
    [self getStaffShopList];
    
}
@end
