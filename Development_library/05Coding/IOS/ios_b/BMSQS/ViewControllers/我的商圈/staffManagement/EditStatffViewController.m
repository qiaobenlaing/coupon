//
//  EditStatffViewController.m
//  BMSQS
//
//  Created by liuqin on 15/11/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "EditStatffViewController.h"
#import "BaomiViewUtils.h"
@interface EditStatffViewController ()<UITableViewDelegate,UITableViewDataSource>


@property (nonatomic, strong)UITableView *staffTableView;

@property (nonatomic, strong)NSDictionary *manager;
@property (nonatomic, strong)NSMutableArray *shopList;

@property (nonatomic, strong)NSMutableArray *shopSeleList;

@end

@implementation EditStatffViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"门面设置";
    
    [self setNavTitle:@"门面设置"];
    [self setNavBackItem];
    
    
    self.shopList = [[NSMutableArray alloc]init];
    self.shopSeleList = [[NSMutableArray alloc]init];

    
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [btnback setTitle:@"保存" forState:UIControlStateNormal];
    [btnback setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [btnback addTarget:self action:@selector(editOwner) forControlEvents:UIControlEventTouchUpInside];
    
    [self setNavRightBarItem:btnback];
    
    
//    UIBarButtonItem *backButtonItem = [[UIBarButtonItem alloc] initWithCustomView:btnback];
//    self.navigationItem.rightBarButtonItem = backButtonItem;

    
    self.staffTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-60)];
    self.staffTableView.backgroundColor = [UIColor clearColor];
    self.staffTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.staffTableView.delegate = self;
    self.staffTableView.dataSource = self;
    [self.view addSubview:self.staffTableView];
    [self getStoreBelong];
    
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.section ==0){
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
     
     if ([self.manager objectForKey:@"realName"]) {
           label.text = [NSString stringWithFormat:@"  %@:%@",[self.manager objectForKey:@"realName"],[self.manager objectForKey:@"mobileNbr"]];
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
            
            
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 43)];
            bgView.backgroundColor  = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            
            UIButton *btn = [BaomiViewUtils creatbtn:CGRectMake(10, 10, 20, 20) StateNormalImage:[UIImage imageNamed:@""] StateSelectedImage:[UIImage imageNamed:@"first"] tag:200];
            btn.backgroundColor = self.view.backgroundColor;
            btn.layer.borderColor = [UICOLOR(182, 0, 12, 1.0) CGColor];
            btn.layer.borderWidth = 1;
            btn.enabled = NO;
            [bgView addSubview:btn];
            
            
            UILabel *shopLabel = [[UILabel alloc]initWithFrame:CGRectMake(40, 0, APP_VIEW_WIDTH-60, 43)];
            shopLabel.textColor = [UIColor blackColor];
            shopLabel.tag = 100;
            shopLabel.font = [UIFont systemFontOfSize:14.f];
            shopLabel.backgroundColor = [UIColor clearColor];
            [bgView addSubview:shopLabel];
            
        }
        
        UILabel *label = (UILabel *)[cell viewWithTag:100];
        UIButton *btn = (UIButton *)[cell viewWithTag:200];
        NSString *key = [self.shopSeleList objectAtIndex:indexPath.row];
        if ([key isEqualToString:@"0"]) {
            btn.backgroundColor = self.view.backgroundColor;
        }else{
            btn.backgroundColor = [UIColor redColor];
        }
    

        
        NSDictionary *shopDic = [self.shopList objectAtIndex:indexPath.row];
        if ([shopDic objectForKey:@"shopName"]) {
            label.text = [shopDic objectForKey:@"shopName"];
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
        return self.manager.count>0?1:0;
    }else if (section ==1){
        return self.shopList.count;
    }
    
    return 10;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        return  50;
    }else if(indexPath.section ==1){
        
        return 44;
        
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
        label.text = @"  店长";
    }else{
        label.text = @"  门店管理";
    }
    [headView addSubview:label];
    
    
    return headView;
    
    
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 2;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    UIButton *btn = (UIButton *)[cell viewWithTag:200];
    
    NSString *key = [self.shopSeleList objectAtIndex:indexPath.row];
    
    if ([key isEqualToString:@"0"]) {
        [self.shopSeleList replaceObjectAtIndex:indexPath.row withObject:@"1"];
        btn.backgroundColor = [UIColor redColor];

    }else{
        [self.shopSeleList replaceObjectAtIndex:indexPath.row withObject:@"0"];
        btn.backgroundColor = self.view.backgroundColor;

    }
    
    
}

-(void)getStoreBelong{
    
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.staffCode forKey:@"staffCode"];              //修改店长
    [params setObject:@"1" forKey:@"page"];                          //空
    
    
    NSString* vcode = [gloabFunction getSign:@"getStoreBelong" strParams:self.staffCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __block typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"getStoreBelong" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        
        if ([responseObject objectForKey:@"manager"]) {
            weakSelf.manager = [responseObject objectForKey:@"manager"];
            NSArray *list = [responseObject objectForKey:@"shopList"];
            [weakSelf.shopList addObjectsFromArray:list];
            
            for (NSDictionary *dic in list) {
                [weakSelf.shopSeleList addObject:@"0"];
            }
            
            [weakSelf.staffTableView reloadData];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
    
}

#pragma mark --保存--
-(void)editOwner{
    NSLog(@"保存");
    NSMutableString *seleShopCodeS = [[NSMutableString alloc]init];
    for (int i =0;i<self.shopList.count;i++){
        NSString *key = [self.shopSeleList objectAtIndex:i];
        if ([key isEqualToString:@"0"]) {
            [seleShopCodeS appendFormat:@"|"];
        }
        if ([key isEqualToString:@"1"]) {
            NSDictionary *dic = [self.shopList objectAtIndex:i];
            [seleShopCodeS appendFormat:@"%@|",[dic objectForKey:@"shopCode"]];
        }
     }
    [seleShopCodeS deleteCharactersInRange:NSMakeRange(seleShopCodeS.length-1, 1)];
    
    
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.staffCode forKey:@"staffCode"];              //修改店长
    [params setObject:seleShopCodeS forKey:@"shopCode"];                          //空
    
    
    NSString* vcode = [gloabFunction getSign:@"editOwner" strParams:self.staffCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __block typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"editOwner" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        
        if ([responseObject objectForKey:@"code"]) {
            int code =(int) [[responseObject objectForKey:@"code"]integerValue];
            if (code ==50000 ) {
                 [[NSNotificationCenter defaultCenter]postNotificationName:@"editStaff" object:nil];
                 [weakSelf.navigationController popViewControllerAnimated:YES];
            }
           
        }else{
            
            
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];

}

@end
