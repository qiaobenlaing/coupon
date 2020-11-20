//
//  BMSQ_BuyOrderController.m
//  BMSQC
//
//  Created by djx on 15/8/7.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_BuyOrderController.h"
#import "BMSQ_BuyEndController.h"
#import "BMSQ_BuyOrderCuponController.h"

@interface BMSQ_BuyOrderController ()
{
    UITableView* m_tableView;
    NSMutableDictionary* m_dicBonus; //红包
    UITextField* tx_buyPrice;
    UITextField* tx_shopBonus;
    NSMutableDictionary* m_dicCupon; //选择的优惠券
    UITextField* tx_cupons;
    UITextField* tx_bmBonus; //惠圈红包
}

@end

@implementation BMSQ_BuyOrderController

@synthesize shopCode;
@synthesize shopName;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self setViewUp];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"买单"];
    
    m_dicBonus = [[NSMutableDictionary alloc]init];
    m_dicCupon = [[NSMutableDictionary alloc]init];
    
    tx_buyPrice = [[UITextField alloc]initWithFrame:CGRectMake(80, 0, APP_VIEW_WIDTH-130, 43)];
    tx_buyPrice.textColor = UICOLOR(182, 0, 12, 1.0);
    tx_buyPrice.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
    tx_buyPrice.returnKeyType = UIReturnKeyDone;
    tx_buyPrice.delegate = self;
    
    tx_shopBonus = [[UITextField alloc]initWithFrame:CGRectMake(80, 0, APP_VIEW_WIDTH/2-100, 43)];
    tx_shopBonus.textColor = UICOLOR(182, 0, 12, 1.0);
    tx_shopBonus.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
    tx_shopBonus.returnKeyType = UIReturnKeyDone;
    tx_shopBonus.delegate = self;
    
    tx_bmBonus = [[UITextField alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2+80, 0, APP_VIEW_WIDTH/2-100, 43)];
    tx_bmBonus.textColor = UICOLOR(182, 0, 12, 1.0);
    tx_bmBonus.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
    tx_bmBonus.returnKeyType = UIReturnKeyDone;
    tx_bmBonus.delegate = self;
    
    tx_cupons = [[UITextField alloc]initWithFrame:CGRectMake(80, 0, APP_VIEW_WIDTH-180, 43)];
    tx_cupons.textColor = UICOLOR(182, 0, 12, 1.0);
    tx_cupons.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
    tx_cupons.returnKeyType = UIReturnKeyDone;
    tx_cupons.clearButtonMode = UITextFieldViewModeWhileEditing;
    tx_cupons.delegate = self;
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    [self userTotalBonusValue];
    
}

- (void)userTotalBonusValue
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:shopCode forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"userTotalBonusValue" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"userTotalBonusValue" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        m_dicBonus = [[NSMutableDictionary alloc]initWithDictionary:responseObject];
        [ProgressManage closeProgress];
        
        tx_bmBonus.text = [NSString stringWithFormat:@"%@",[m_dicBonus objectForKey:@"platBonus"]];
        tx_shopBonus.text = [NSString stringWithFormat:@"%@",[m_dicBonus objectForKey:@"shopBonus"]];
        
        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        
    }];
}


#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 5;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 0)
    {
        return 64;
    }
    else if(indexPath.row == 4)
    {
        return 150;
    }
    
    return 44;
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //操作区域
    static NSString *cellIdentifier = @"Cell";
    UITableViewCell *cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
    //if (cell == nil)
    {
        
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    cell.backgroundColor = APP_VIEW_BACKCOLOR;
    cell.selectionStyle=UITableViewCellSelectionStyleNone ;

    //    [cell setCellValue:[m_dataSource objectAtIndex:indexPath.row] indexPath:indexPath];
    if (indexPath.row == 0)
    {
        UIView* iv_back = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 44)];
        iv_back.backgroundColor = [UIColor whiteColor];
        [cell addSubview:iv_back];
        
        UILabel* lb_title = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 100, 44)];
        lb_title.text = @"商家名称";
        lb_title.font = [UIFont systemFontOfSize:14];
        lb_title.textColor = [UIColor blackColor];
        [iv_back addSubview:lb_title];
        
        UILabel* lb_content = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 200, 0, 190, 44)];
        lb_content.textColor = [UIColor blackColor];
        lb_content.font = [UIFont systemFontOfSize:14];
        lb_content.textAlignment = NSTextAlignmentRight;
        lb_content.text = [gloabFunction changeNullToBlank:shopName];
        [iv_back addSubview:lb_content];
        
    }
    else if(indexPath.row == 1)
    {
        UILabel* lb_title = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 100, 44)];
        lb_title.text = @"消费金额";
        lb_title.font = [UIFont systemFontOfSize:14];
        lb_title.textColor = [UIColor blackColor];
        [cell addSubview:lb_title];
        
        [cell addSubview:tx_buyPrice];
        
        UILabel* lb_unit = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 30, 0, 25, 43)];
        lb_unit.text = @"元";
        lb_unit.font = [UIFont systemFontOfSize:14];
        [cell addSubview:lb_unit];
        cell.backgroundColor = [UIColor whiteColor];
        
        UIView* line = [[UIView alloc]initWithFrame:CGRectMake(0, 43, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
        line.backgroundColor = APP_CELL_LINE_COLOR;
        [cell addSubview:line];
        
    }
    else if(indexPath.row == 2)
    {
        UILabel* lb_title = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 100, 44)];
        lb_title.text = @"优惠券";
        lb_title.font = [UIFont systemFontOfSize:14];
        lb_title.textColor = [UIColor blackColor];
        [cell addSubview:lb_title];
        cell.backgroundColor = [UIColor whiteColor];
        
        [cell addSubview:tx_cupons];
        
        if (m_dicCupon != nil && m_dicCupon.count > 0)
        {
            UILabel* lb_unit = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 40, 0, 25, 43)];
            
            if ([[m_dicCupon objectForKey:@"discountPercent"] floatValue] > 0)
            {
                lb_unit.text = @"折";
                tx_cupons.text = [NSString stringWithFormat:@"%.2f",[[m_dicCupon objectForKey:@"discountPercent"] floatValue]/10];
            }
            else
            {
                lb_unit.text = @"元";
                tx_cupons.text = [NSString stringWithFormat:@"%@",[m_dicCupon objectForKey:@"insteadPrice"]];
            }
            
            lb_unit.font = [UIFont systemFontOfSize:14];
            [cell addSubview:lb_unit];
            
            
        }
        
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        
        UIView* line = [[UIView alloc]initWithFrame:CGRectMake(0, 43, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
        line.backgroundColor = APP_CELL_LINE_COLOR;
        [cell addSubview:line];
    }
    else if(indexPath.row == 3)
    {
        cell.backgroundColor = [UIColor whiteColor];
        UILabel* lb_title = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 100, 44)];
        lb_title.text = @"商家红包";
        lb_title.font = [UIFont systemFontOfSize:14];
        lb_title.textColor = [UIColor blackColor];
        [cell addSubview:lb_title];
        
        [cell addSubview:tx_shopBonus];
        
        UILabel* lb_unit = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2 - 30, 0, 25, 43)];
        lb_unit.text = @"元";
        lb_unit.font = [UIFont systemFontOfSize:14];
        [cell addSubview:lb_unit];
        
        UILabel* lb_title1 = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2+10, 0, 100, 44)];
        lb_title1.text = @"惠圈红包";
        lb_title1.font = [UIFont systemFontOfSize:14];
        lb_title1.textColor = [UIColor blackColor];
        [cell addSubview:lb_title1];
        
        [cell addSubview:tx_bmBonus];
        
        UILabel* lb_unit2 = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 30, 0, 25, 43)];
        lb_unit2.text = @"元";
        lb_unit2.font = [UIFont systemFontOfSize:14];
        [cell addSubview:lb_unit2];
    }
    else
    {
        UIButton* btnBuy = [[UIButton alloc]initWithFrame:CGRectMake(10, 20, APP_VIEW_WIDTH-20, 40)];
        btnBuy.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        [btnBuy addTarget:self action:@selector(btnBuyClick) forControlEvents:UIControlEventTouchUpInside];
        [btnBuy setTitle:@"下一步" forState:UIControlStateNormal];
        [btnBuy setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [cell addSubview:btnBuy];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0 && indexPath.row == 2)
    {
        if(tx_buyPrice.text == nil || tx_buyPrice.text.length <= 0)
        {
            [self showAlertView:@"请输入支付金额"];
            return;
        }
        BMSQ_BuyOrderCuponController* cuponCtrl = [[BMSQ_BuyOrderCuponController alloc]init];
        cuponCtrl.delegate = self;
        cuponCtrl.shopCode = shopCode;
        cuponCtrl.consumeAmount = tx_buyPrice.text;
        [self.navigationController pushViewController:cuponCtrl animated:YES];
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

- (void)btnBuyClick
{
    if(tx_buyPrice.text == nil || tx_buyPrice.text.length <= 0)
    {
        [self showAlertView:@"请输入支付金额"];
        return;
    }
    
    if(tx_bmBonus.text == nil || tx_bmBonus.text.length <= 0 || tx_bmBonus.text.floatValue > [[m_dicBonus objectForKey:@"platBonus"] floatValue])
    {
        [self showAlertView:@"红包金额大于可用红包金额"];
        return;
    }
    
    if(tx_shopBonus.text == nil || tx_shopBonus.text.length <= 0 || tx_shopBonus.text.floatValue > [[m_dicBonus objectForKey:@"shopBonus"] floatValue])
    {
        [self showAlertView:@"商家红包金额大于可用商家红包金额"];
        return;
    }
    
    BMSQ_BuyEndController* buyCtrl = [[BMSQ_BuyEndController alloc]init];
    buyCtrl.shopName = shopName;
    buyCtrl.payCount = tx_buyPrice.text;
    buyCtrl.payDisCount = [NSString stringWithFormat:@"%.2f",tx_shopBonus.text.floatValue+tx_bmBonus.text.floatValue];
    float cupons = 0.00;
    if (tx_cupons.text != nil && tx_cupons.text.length > 0)
    {
        cupons = tx_cupons.text.floatValue;
    }
    buyCtrl.payMore = [NSString stringWithFormat:@"%.2f",tx_buyPrice.text.floatValue - tx_shopBonus.text.floatValue - tx_bmBonus.text.floatValue - cupons];
    [self.navigationController pushViewController:buyCtrl animated:YES];
}

- (void)getCuponData:(NSDictionary*)dicCupon
{
    m_dicCupon = [[NSMutableDictionary alloc]initWithDictionary:dicCupon];
    [m_tableView reloadData];
}

@end
