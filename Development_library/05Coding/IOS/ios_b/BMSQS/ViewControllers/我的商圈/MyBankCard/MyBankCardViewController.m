//
//  MyBankCardViewController.m
//  BMSQS
//
//  Created by gh on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "MyBankCardViewController.h"
#import "SVProgressHUD.h"
#import "MyBackCardCell.h"
#import "AddBankCardViewController.h"

@interface MyBankCardViewController ()<UITableViewDataSource, UITableViewDelegate>
{
    UITableView* m_tableView;
    NSMutableArray* m_dataSource;
    int pageNumber; //当前页码
    
}

@property (nonatomic, strong)NSDictionary *deleDic;


@end

@implementation MyBankCardViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(refreshMyCard) name:@"refreshMyCard" object:nil];
    
    [self setViewUp];
    
}

-(void)refreshMyCard{
    
    [self getShopCardList];

}


- (void)setViewUp {
    [self setNavBackItem];
    [self setNavTitle:@"我的银行卡"];
    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(getShopCardList) name:@"addCardSure" object:nil];
    
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    pageNumber = 1;
    m_dataSource = [[NSMutableArray alloc]init];
    
    
    [self getShopCardList];
}

#pragma mark - UITableView delegate
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

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section == 1)
    {
        return 1;
    }else if (section == 2){
        return 1;
    }

    if (section == 0) {
//        return 1;
        return m_dataSource.count;
    }
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    //操作区域
    if (indexPath.section == 0)
    {
        static NSString *cellIdentifier = @"cardCell";
        MyBackCardCell *cell = (MyBackCardCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            
            cell = [[MyBackCardCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];

        }
        cell.backgroundColor = [UIColor whiteColor];
        cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        [cell setCellValue:[m_dataSource objectAtIndex:indexPath.row]];
        return cell;

    }
    else if (indexPath.section == 2){
        static NSString *cellIdentifier = @"Cell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.backgroundColor = [UIColor clearColor];
            UIImageView *bottomImage = [[UIImageView alloc]initWithFrame:CGRectMake(0,10, APP_VIEW_WIDTH, 110)];
            bottomImage.backgroundColor = [UIColor whiteColor];
            [bottomImage setImage:[UIImage imageNamed:@"login_footer"]];
            [cell addSubview:bottomImage];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
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
    
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 1)
    {
        AddBankCardViewController* addCtrl = [[AddBankCardViewController alloc]init];
        addCtrl.fromvc = (int)self.navigationController.viewControllers.count;
        [self.navigationController pushViewController:addCtrl animated:YES];
    }else {
        
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

//alertview
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    if (buttonIndex ==0) {  //dele
        
        [SVProgressHUD showWithStatus:@""];
        [self initJsonPrcClient:@"1"];
        NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
        [params setObject:[self.deleDic objectForKey:@"bankAccountCode"] forKey:@"bankAccountCode"];
        NSString* vcode = [gloabFunction getSign:@"terminateBankAccount" strParams:[self.deleDic objectForKey:@"bankAccountCode"]];
        [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
        [params setObject:vcode forKey:@"vcode"];
        [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
        
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
                CSAlert(errorMsg);
            }
            
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            [SVProgressHUD dismiss];
            
//            [ProgressManage closeProgress];
        }];
        
        
    }else{
        
    }
    
}




//获取银行卡列表
- (void)getShopCardList {
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@"" maskType:SVProgressHUDMaskTypeClear];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[NSNumber numberWithInt:pageNumber] forKey:@"page"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopCardList" strParams:[gloabFunction getShopCode]];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];

    
    [self.jsonPrcClient invokeMethod:@"getShopCardList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        [m_dataSource removeAllObjects];
        
        [m_dataSource addObjectsFromArray:[responseObject objectForKey:@"list"]];
        [m_tableView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(@"网络错误，请重试");
        [SVProgressHUD dismiss];
        
    }];
    
    
}




- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
