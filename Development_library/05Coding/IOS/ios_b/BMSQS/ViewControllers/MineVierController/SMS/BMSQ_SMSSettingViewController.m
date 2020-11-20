//
//  BMSQ_SMSSettingViewController.m
//  BMSQS
//
//  Created by liuqin on 16/2/24.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_SMSSettingViewController.h"
#import "BMSQ_SMSaddViewController.h"

#import "SVProgressHUD.h"

@interface BMSQ_SMSSettingViewController ()<UITableViewDataSource,UITableViewDelegate>


@property (nonatomic, strong)UITableView *contactTable;
@property (nonatomic, strong)NSMutableArray *MSMArray;


@end


@implementation BMSQ_SMSSettingViewController

-(void)viewDidLoad{
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavTitle:@"短信设置"];
    self.MSMArray = [[NSMutableArray alloc]init];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(refreshContat) name:@"refreshContat" object:nil];
    
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [btnback setTitle:@"增加" forState:0];
    btnback.titleLabel.font = [UIFont boldSystemFontOfSize:15];
    [btnback addTarget:self action:@selector(clickRight:) forControlEvents:UIControlEventTouchUpInside];

    [self setNavRightBarItem:btnback];
    
    
    self.contactTable = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.contactTable.backgroundColor = [UIColor clearColor];
    self.contactTable.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.contactTable.delegate = self;
    self.contactTable.dataSource = self;
    [self.view addSubview:self.contactTable];

    
    
    [self getMRecipientList];

}
-(BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath{
    return YES;
}
-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        NSDictionary *dic = [self.MSMArray objectAtIndex:indexPath.row];
        [self delMRecipient:[NSString stringWithFormat:@"%@",[dic objectForKey:@"recipientId"]]];

        [self.MSMArray removeObjectAtIndex:indexPath.row];
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
        
    }
    
}


-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString *identifier = @"contactIdetifier";
    UITableViewCell *cell =[tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        UIView *bottomLine = [[UIView alloc]initWithFrame:CGRectMake(5, 49, APP_VIEW_WIDTH-10, 0.3)];
        bottomLine.alpha = 0.3;
        bottomLine.backgroundColor = APP_TEXTCOLOR;
        [cell addSubview:bottomLine];
        cell.textLabel.textColor = APP_TEXTCOLOR;
        cell.textLabel.font = [UIFont systemFontOfSize:15.f];
        
    }
    NSDictionary *dic = [self.MSMArray objectAtIndex:indexPath.row];
    
    cell.textLabel.text = [NSString stringWithFormat:@"姓名:%@   电话号码:%@",[dic objectForKey:@"staffName"],[dic objectForKey:@"mobileNbr"]];
//@"姓名:习近平  电话号码:18667115776";]
    return cell;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.MSMArray.count;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 50;
}
#pragma mark --增加
-(void)clickRight:(UIButton *)button{
    
    BMSQ_SMSaddViewController *vc = [[BMSQ_SMSaddViewController alloc]init];
    [self.navigationController pushViewController:vc animated:YES];
}



-(void)getMRecipientList{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"getMRecipientList" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getMRecipientList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        [weakSelf.MSMArray addObjectsFromArray:responseObject];
        [weakSelf.contactTable reloadData];
        
        if (weakSelf.MSMArray.count<=0) {
            weakSelf.noDateImage.hidden = NO;
        }else{
            weakSelf.noDateImage.hidden = YES;

        }
        
        [SVProgressHUD dismiss];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}
-(void)delMRecipient:(NSString *)recipientId{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:recipientId forKey:@"recipientId"];
    NSString* vcode = [gloabFunction getSign:@"delMRecipient" strParams:recipientId];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;

    [self.jsonPrcClient invokeMethod:@"delMRecipient" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if (weakSelf.MSMArray.count<=0) {
            weakSelf.noDateImage.hidden = NO;
        }
        [SVProgressHUD dismiss];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}
#pragma mark 接收到的通知
-(void)refreshContat{
    [self.MSMArray removeAllObjects];

    [self getMRecipientList];
}
@end
