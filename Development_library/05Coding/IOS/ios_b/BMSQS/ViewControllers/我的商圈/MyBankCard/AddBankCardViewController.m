//
//  AddBankCardViewController.m
//  BMSQS
//
//  Created by gh on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "AddBankCardViewController.h"
#import "AddBankCardCell.h"
#import "SVProgressHUD.h"
#import "AddBankSureViewController.h"

@interface AddBankCardViewController () <UITableViewDataSource, UITableViewDelegate>
{
    UITableView* m_tableView;
    NSMutableArray* m_dataSource;
    
}

@end

@implementation AddBankCardViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
}

- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"添加银行卡"];
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y - 10)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    m_dataSource = [[NSMutableArray alloc]initWithObjects:@{@"title":@"姓名",@"content":@"请输入持卡人姓名"},@{@"title":@"证件号码",@"content":@"身份证号码"},@{@"title":@"卡号",@"content":@"请输入银行卡号"},@{@"title":@"手机号码",@"content":@"请输入办卡时留的手机号码"}, nil];
    
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(section == 1)
        return 1;
    else if (section ==2)
        return 1;
    else
        return m_dataSource.count;
    
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 1)
    {
        return 150;
    }if (indexPath.section == 2){
        return 120;
        
    }
    
    return 44;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
    {
        
        static NSString *cellIdentifier = @"Cell";
        AddBankCardCell *cell = (AddBankCardCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            
            cell = [[AddBankCardCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.backgroundColor = [UIColor whiteColor];
            cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        }
        
        if(indexPath.row == 3 || indexPath.row == 2){
            cell.tx_content.keyboardType = UIKeyboardTypeDecimalPad;
            
        }else{
            cell.tx_content.keyboardType =UIKeyboardTypeDefault;
        }
        cell.tx_content.tag = 100+indexPath.row;
        [cell.tx_content addTarget:self action:@selector(changeLength:) forControlEvents:UIControlEventEditingChanged];
        
        NSDictionary* dic = [m_dataSource objectAtIndex:indexPath.row];
        [cell setCellValue:[dic objectForKey:@"title"] content:[dic objectForKey:@"content"]];
        
        return cell;
        
    }else if(indexPath.section ==2){
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
        static NSString *cellIdentifier = @"Cell";
        UITableViewCell *cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        }
        
        cell.backgroundColor = [UIColor whiteColor];
        cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        
        UIButton* btnAddCard = [[UIButton alloc]initWithFrame:CGRectMake(15, 15, APP_VIEW_WIDTH - 30, 44)];
        [btnAddCard setTitle:@"下一步" forState:UIControlStateNormal];
        btnAddCard.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        [btnAddCard setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [btnAddCard addTarget:self action:@selector(btnAddCardClick) forControlEvents:UIControlEventTouchUpInside];
        [btnAddCard setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [cell addSubview:btnAddCard];
        
        return cell;
    }
    
}

- (void)btnAddCardClick
{
    
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:0 inSection:0];
    AddBankCardCell *cell=(AddBankCardCell *)[m_tableView cellForRowAtIndexPath:indexPath];
    NSString *name =cell.tx_content.text;
    
    
    if (name.length == 0) {
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"请输入持卡人姓名" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [alterView show];
    }
    
    
    indexPath=[NSIndexPath indexPathForRow:1 inSection:0];
    cell=(AddBankCardCell *)[m_tableView cellForRowAtIndexPath:indexPath];
    NSString *idNbr =cell.tx_content.text;
    
    if (idNbr.length == 0) {
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"请输入证件号" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [alterView show];
    }
    
    indexPath=[NSIndexPath indexPathForRow:2 inSection:0];
    cell=(AddBankCardCell *)[m_tableView cellForRowAtIndexPath:indexPath];
    NSString *cardId =cell.tx_content.text;
    
    
    if (cardId.length == 0) {
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"请输入卡号" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [alterView show];
    }
    
    
    indexPath=[NSIndexPath indexPathForRow:3 inSection:0];
    cell=(AddBankCardCell *)[m_tableView cellForRowAtIndexPath:indexPath];
    NSString *mobileNbr =cell.tx_content.text;
    
#pragma mark - 添加工行卡
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:name forKey:@"accountName"];
    [params setObject:@"0" forKey:@"idType"];
    [params setObject:idNbr forKey:@"idNbr"];
    [params setObject:cardId forKey:@"bankCard"];
    
    [params setObject:mobileNbr.length>0?mobileNbr:@"" forKey:@"mobileNbr"];
    NSString* vcode = [gloabFunction getSign:@"shopAddBankAccount" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"shopAddBankAccount" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        int i =(int) [[responseObject objectForKey:@"code"]integerValue];
        
        if (i == 50000) {
            
            NSIndexPath *indexPath=[NSIndexPath indexPathForRow:3 inSection:0];
            AddBankCardCell *cell=(AddBankCardCell *)[m_tableView cellForRowAtIndexPath:indexPath];
            NSString *mobileNbr =cell.tx_content.text;
            
            AddBankSureViewController* sureCtrl = [[AddBankSureViewController alloc]init];
            sureCtrl.phoneNumber = mobileNbr;
            sureCtrl.orderNbr = [responseObject objectForKey:@"orderNbr"];
            sureCtrl.hidesBottomBarWhenPushed = YES;
            sureCtrl.formvc = wself.fromvc;
            [self.navigationController pushViewController:sureCtrl animated:YES];
            
        }else{
            
            
            if (i == 20000) {
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"失败，请重试"  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterView show];
            }else if(i == 50050){
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"账号姓名不正确"  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterView show];
            }else if (i==50051){
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"证件号码不正确"  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterView show];
            }else if (i==50052){
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"银行卡号不正确"  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterView show];
            }else if (i==50054){
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"证件类型不正确"  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterView show];
            }else if (i==50055){
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"该卡已经签订支付协议"  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterView show];
            }else if (i==50060){
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"银行卡归属的用户数量超限"  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterView show];
            }else if (i==60002){
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"银行预留手机号码不正确"  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterView show];
            }else{
                
                NSString *meessage= [NSString stringWithFormat:@"错误[%d]编号",i];
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:meessage  delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterView show];
            }
            
            
            
            
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
    }];
    
}

-(void)changeLength:(UITextField *)textField{
    if (textField.tag ==100) {
        if (textField.text.length>10) {
            textField.text = [textField.text substringToIndex:10];
        }
    }else if (textField.tag == 103){
        if (textField.text.length>=11) {
            textField.text = [textField.text substringToIndex:11];
            [textField resignFirstResponder];
        }
        
    }
    
}

@end
