//
//  BMSQ_MyBankCardController.m
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MyBankCardController.h"
#import "BMSQ_MyBankCell.h"
#import "BMSQ_MyBankDetailController.h"
#import "BMSQ_AddBankCardController.h"
#import "SVProgressHUD.h"
#import "BMSQ_setMyCardViewController.h"
#import "MobClick.h"
@interface BMSQ_MyBankCardController ()<UIAlertViewDelegate>
{
    UITableView* m_tableView;
    NSMutableArray* m_dataSource;
    int pageNumber; //当前页码
    BOOL refreshing;
}

@property (nonatomic, strong)NSDictionary *deleDic;

@end

@implementation BMSQ_MyBankCardController


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"MyBankCard"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"MyBankCard"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(refreshMyCard:) name:@"refreshMyCard" object:nil];
}

-(void)refreshMyCard:(NSNotification *)notification{
    NSArray *array = notification.object;
    [m_dataSource removeAllObjects];
    [m_dataSource addObjectsFromArray:array];
    [m_tableView reloadData];
}


- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"我的银行卡"];
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    pageNumber = 1;
    m_dataSource = [[NSMutableArray alloc]init];

    
    [self getBankAccountList];
    
}



- (void)loadData{
    
    
    if (refreshing)
    {
        pageNumber = 1;
        refreshing = NO;
    }
    else
    {
        pageNumber++;
    }
    
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        
        [self getBankAccountList];
    });
    
}

#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 1)
    {
        return 1;
    }else if (section == 2){
        return 1;
    }
    return m_dataSource.count;
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
    {
        return 62;
    }else if (indexPath.section == 2){
        return 120;
    }
    return 40;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //操作区域
    if (indexPath.section == 0)
    {
        static NSString *cellIdentifier = @"cardCell";
        BMSQ_MyBankCell *cell = (BMSQ_MyBankCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            
            cell = [[BMSQ_MyBankCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
//            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
        
        cell.backgroundColor = [UIColor whiteColor];
        cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        [cell setCellValue:[m_dataSource objectAtIndex:indexPath.row]];
        return cell;
    }else if (indexPath.section == 2){
        static NSString *cellIdentifier = @"Cell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.backgroundColor = [UIColor clearColor];
           UIImageView *bottomImage = [[UIImageView alloc]initWithFrame:CGRectMake(0,10, APP_VIEW_WIDTH, 110)];
            [bottomImage setImage:[UIImage imageNamed:@"login_footer"]];
            [cell addSubview:bottomImage];

        }
        

        return cell;
    }
    
    else
    {
        static NSString* cellIdentify = @"cellIdentify3";
        UITableViewCell* cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:@"开通工行卡惠支付*仅支持工行卡*"];
            
            [str addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(8,8)];
            
            UILabel *lb_addCard = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
            lb_addCard.backgroundColor = [UIColor whiteColor];
            lb_addCard.attributedText = str;
            lb_addCard.textAlignment = NSTextAlignmentCenter;
            lb_addCard.font = [UIFont systemFontOfSize:12];
            [cell addSubview:lb_addCard];
        }
        
        
        return cell;
    }

}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 1)
    {
        BMSQ_AddBankCardController* addCtrl = [[BMSQ_AddBankCardController alloc]init];
        addCtrl.fromvc = (int)self.navigationController.viewControllers.count;
        [self.navigationController pushViewController:addCtrl animated:YES];
    }else{
        
        if (self.ispayCard ||self.isPayQR) {
            NSDictionary *dic = [m_dataSource objectAtIndex:indexPath.row];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"seleCard" object:dic];
            [self.navigationController popViewControllerAnimated:YES];
        }
       
    }
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section==0) {
        
        if (self.isCanDele) {
            return NO;

        }else{
            return YES;
        }
    }else{
        return NO;
    }
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section ==0) {
        return UITableViewCellEditingStyleDelete;

    }else{
        return UITableViewCellEditingStyleNone;
    }
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{
    
    self.deleDic = [m_dataSource objectAtIndex:indexPath.row];
    
    UIAlertView *alterview = [[UIAlertView alloc]initWithTitle:nil message:@"确定要解除协议吗" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:@"取消", nil];
    [alterview show];
    
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    if (buttonIndex ==0) {  //dele
        
        [SVProgressHUD showWithStatus:@""];
        [self initJsonPrcClient:@"2"];
        NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
        [params setObject:[self.deleDic objectForKey:@"bankAccountCode"] forKey:@"bankAccountCode"];
        NSString* vcode = [gloabFunction getSign:@"terminateBankAccount" strParams:[self.deleDic objectForKey:@"bankAccountCode"]];
        [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
        [params setObject:vcode forKey:@"vcode"];
        [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
        
        [ProgressManage openProgressText:nil];
        [self.jsonPrcClient invokeMethod:@"terminateBankAccount" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
            [SVProgressHUD dismiss];
            if ([[responseObject objectForKey:@"code"]integerValue]== 50000) {
                [m_dataSource removeObject:self.deleDic];
                [m_tableView reloadData];

            }else{
                int code =(int)[[responseObject objectForKey:@"code"]integerValue];
                NSString *errorMsg;
                if (code == 20000) {
                    errorMsg = [NSString stringWithFormat:@"失败，请重试[%d]",code];
                }else if (code == 50057){
                    errorMsg = [NSString stringWithFormat:@"编码错误[%d]",code];

                }else if(code == 50059){
                    errorMsg = [NSString stringWithFormat:@"银行卡已经解除协议了[%d]",code];

                }else{
                    errorMsg = [NSString stringWithFormat:@"解梆失败原因[%d]，请联系服务人员",code];

                }
                
            }
            
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            [SVProgressHUD dismiss];

            [ProgressManage closeProgress];
        }];
        
        
    }else{
        
    }
    
}

- (void)getBankAccountList
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[NSNumber numberWithInt:pageNumber] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"getBankAccountList" strParams:[NSString stringWithFormat:@"%@",[gloabFunction getUserCode]]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getBankAccountList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        
        if (pageNumber == 1)
        {
            [m_dataSource removeAllObjects];
        }
        
        [m_dataSource addObjectsFromArray:[responseObject objectForKey:@"bankAccountList"]];
        
        
        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}

-(void)goBack{
    if (self.isPayQR && m_dataSource.count==1) {
        [[NSNotificationCenter defaultCenter]postNotificationName:@"loadMyCard" object:nil];
    }
    [self.navigationController popViewControllerAnimated:YES];
}

@end
