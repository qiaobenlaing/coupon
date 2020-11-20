//
//  BMSQ_BenefitCardDetailController.m
//  BMSQC
//
//  Created by djx on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_BenefitCardDetailController.h"
#import "BMSQ_ShopDetailController.h"
#import "MobClick.h"
@interface BMSQ_BenefitCardDetailController ()
{
    UITableView* m_tableView;
    NSDictionary* m_dicCardInfo;
}

@end

@implementation BMSQ_BenefitCardDetailController
@synthesize userCardCode;
@synthesize isAttention;

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"BenefitCardDetail"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"BenefitCardDetail"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
    
    if (![gloabFunction isLogin])
    {
        [self getLogin];
        return;
    }
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
    [self setNavTitle:@"会员卡详情"];
    
    m_dicCardInfo = [[NSMutableDictionary alloc]init];
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    [self getUserCardInfo];

}

- (void)getUserCardInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:userCardCode forKey:@"userCardCode"];
    NSString* vcode = [gloabFunction getSign:@"getUserCardInfo" strParams:userCardCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getUserCardInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        m_dicCardInfo = [[NSMutableDictionary alloc]initWithDictionary:responseObject];
        [ProgressManage closeProgress];

        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        
    }];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 5;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0 || section == 1 || section == 4 || section == 2)
    {
        return 1;
    }

    
    return 2;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
    {
        return 170;
    }
    
    if (indexPath.section == 1 || indexPath.section == 4)
    {
        return 54;
    }
    return 44;
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)
//
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    //操作区域
    if (indexPath.section == 0)
    {
        static NSString* cellIdentify = @"cellIdentify0";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor whiteColor];
        UIImageView* iv_cardBack = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20, 150)];
        NSString* cardName = [m_dicCardInfo objectForKey:@"cardName"];
        if ([cardName isEqualToString:@"银卡"])
        {
            iv_cardBack.image = [UIImage imageNamed:@"iv_silverCar2"];
        }
        else if([cardName isEqualToString:@"金卡"])
        {
            iv_cardBack.image = [UIImage imageNamed:@"iv_goldCard2"];
        }
        else if ([cardName isEqualToString:@"白金卡"])
        {
            iv_cardBack.image = [UIImage imageNamed:@"iv_whiteCard2"];
        }
        [cell addSubview:iv_cardBack];
        
        UIImageView* iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(15, 10, 35, 35)];
        [iv_logo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[m_dicCardInfo objectForKey:@"logoUrl"]]]];
        [iv_cardBack addSubview:iv_logo];
        
        UILabel* lb_shopName = [[UILabel alloc]initWithFrame:CGRectMake(60, 10, 150, 35)];
        lb_shopName.backgroundColor = [UIColor clearColor];
        lb_shopName.textColor = [UIColor whiteColor];
        lb_shopName.font = [UIFont systemFontOfSize:14];
        lb_shopName.text = [gloabFunction changeNullToBlank:[m_dicCardInfo objectForKey:@"shopName"]];
        [iv_cardBack addSubview:lb_shopName];
        
        UILabel* lb_carType = [[UILabel alloc]initWithFrame:CGRectMake(iv_cardBack.frame.size.width - 60, 10, 50, 35)];
        lb_carType.backgroundColor = [UIColor clearColor];
        lb_carType.textColor = [UIColor whiteColor];
        lb_carType.textAlignment = NSTextAlignmentRight;
        lb_carType.font = [UIFont systemFontOfSize:14];
        lb_carType.text = [gloabFunction changeNullToBlank:[m_dicCardInfo objectForKey:@"cardName"]];
        [iv_cardBack addSubview:lb_carType];
        
        
        UILabel* lb_carPoint = [[UILabel alloc]initWithFrame:CGRectMake(10, 120, 50, 25)];
        lb_carPoint.backgroundColor = [UIColor clearColor];
        lb_carPoint.textColor = [UIColor whiteColor];
        lb_carPoint.textAlignment = NSTextAlignmentRight;
        lb_carPoint.font = [UIFont systemFontOfSize:14];
        lb_carPoint.text = [NSString stringWithFormat:@"积分: %@",[gloabFunction changeNullToBlank:[m_dicCardInfo objectForKey:@"point"]]];
        [iv_cardBack addSubview:lb_carPoint];
        
        UILabel* lb_carNumber = [[UILabel alloc]initWithFrame:CGRectMake(iv_cardBack.frame.size.width - 180, 120, 170, 25)];
        lb_carNumber.backgroundColor = [UIColor clearColor];
        lb_carNumber.textColor = [UIColor whiteColor];
        lb_carNumber.font = [UIFont systemFontOfSize:14];
        lb_carNumber.textAlignment = NSTextAlignmentRight;
        lb_carNumber.text = [NSString stringWithFormat:@"NO.%@",[gloabFunction changeNullToBlank:[m_dicCardInfo objectForKey:@"cardNbr"]]];
        [iv_cardBack addSubview:lb_carNumber];
        
        return cell;
    }
    else if(indexPath.section == 1)
    {
        static NSString* cellIdentify = @"cellIdentify1";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
        }
        cell.backgroundColor = APP_VIEW_BACKCOLOR;
        UIView* iv_Back = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 44)];
        iv_Back.backgroundColor = [UIColor whiteColor];
        [cell addSubview:iv_Back];
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        UILabel* lb_carPoint = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH, 44)];
        lb_carPoint.backgroundColor = [UIColor clearColor];
        lb_carPoint.textColor = [UIColor whiteColor];
        lb_carPoint.font = [UIFont systemFontOfSize:14];
        //lb_carPoint.text = [NSString stringWithFormat:@"%@积分",[gloabFunction changeNullToBlank:[m_dicCardInfo objectForKey:@"point"]]];
        [iv_Back addSubview:lb_carPoint];
        
        NSMutableAttributedString* strAttribute = [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"%@积分",[gloabFunction changeNullToBlank:[m_dicCardInfo objectForKey:@"point"]]]];
        //货物名称
        [strAttribute addAttribute:NSForegroundColorAttributeName value:UICOLOR(219, 48, 28, 1.0) range:NSMakeRange(0, [[gloabFunction changeNullToBlank:[m_dicCardInfo objectForKey:@"point"]] length])];
        [strAttribute addAttribute:NSForegroundColorAttributeName value:UICOLOR(74, 74, 74, 1.0) range:NSMakeRange([[gloabFunction changeNullToBlank:[m_dicCardInfo objectForKey:@"point"]] length], 2)];
        lb_carPoint.attributedText = strAttribute;
        
        return cell;
    }
    else if(indexPath.section == 2)
    {
        static NSString* cellIdentify = @"cellIdentify2";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
        //if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        UIImageView* iv_zhuang = [[UIImageView alloc]initWithFrame:CGRectMake(10, 15, 17, 14)];
        iv_zhuang.image = [UIImage imageNamed:@"iv_zhuang"];
        [cell addSubview:iv_zhuang];
        
        UILabel* lb_discount = [[UILabel alloc]initWithFrame:CGRectMake(35, 0, APP_VIEW_WIDTH, 44)];
        lb_discount.backgroundColor = [UIColor clearColor];
        lb_discount.textColor = [UIColor whiteColor];
        NSString* discout = [NSString stringWithFormat:@"%.1f",[[m_dicCardInfo objectForKey:@"discount"] floatValue]/10];
        lb_discount.text = [NSString stringWithFormat:@"全场%@折优惠",discout];
        lb_discount.textColor = UICOLOR(134, 134, 134, 1.0);
        lb_discount.font = [UIFont systemFontOfSize:14];
        [cell addSubview:lb_discount];
        return cell;
    }
    else if(indexPath.section == 3)
    {
        static NSString* cellIdentify = @"cellIdentify3";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        UIImageView* iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(10, 15, 17, 14)];
        
        UILabel* lb_content = [[UILabel alloc]initWithFrame:CGRectMake(35, 0, APP_VIEW_WIDTH, 44)];
        lb_content.backgroundColor = [UIColor clearColor];
        lb_content.textColor = [UIColor whiteColor];
        lb_content.textColor = UICOLOR(134, 134, 134, 1.0);
        lb_content.font = [UIFont systemFontOfSize:14];
        [cell addSubview:lb_content];
        
        
        if (indexPath.row == 0)
        {
            iv_logo.image = [UIImage imageNamed:@"iv_shop"];
            lb_content.text = @"店铺详情";
            
            UIView* line = [[UIView alloc]initWithFrame:CGRectMake(10, 44, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
        }
        else
        {
            iv_logo.image = [UIImage imageNamed:@"iv_cardDetail"];
            lb_content.text = @"会员卡说明";
        }
        
        [cell addSubview:lb_content];
        [cell addSubview:iv_logo];
        
        return cell;
    }
    else
    {
        static NSString* cellIdentify = @"cellIdentify";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = APP_VIEW_BACKCOLOR;
        UIButton* btnAttention = [[UIButton alloc]initWithFrame:CGRectMake(15, 10, APP_VIEW_WIDTH-30, 34)];
        btnAttention.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        if (isAttention)
        {
            [btnAttention setTitle:@"取消关注" forState:UIControlStateNormal];
        }
        else
        {
            [btnAttention setTitle:@"关注" forState:UIControlStateNormal];
        }
        
        [btnAttention addTarget:self action:@selector(btnAttentionClick) forControlEvents:UIControlEventTouchUpInside];
        [btnAttention setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [cell addSubview:btnAttention];
        return cell;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 3)
    {
        if (indexPath.row == 0)
        {
//            BMSQ_ShopDetailController* shopDetailCtrl = [[BMSQ_ShopDetailController alloc]init];
//            shopDetailCtrl.shopCode = [m_dicCardInfo objectForKey:@"shopCode"];
//            shopDetailCtrl.couponDic =m_dicCardInfo;
//            [self.navigationController pushViewController:shopDetailCtrl animated:YES];
        }
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section == 2 || section == 3) {
        return 54;
    }
    
    return 0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if (section == 2 || section == 3)
    {
        UIView* v = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 44)];
        v.backgroundColor = APP_VIEW_BACKCOLOR;
        
        UIView* vBack = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 44)];
        vBack.backgroundColor = [UIColor whiteColor];
        
        UILabel* lbTitle = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH - 10, 44)];
        lbTitle.backgroundColor = [UIColor clearColor];
        lbTitle.textColor = UICOLOR(134, 134, 134, 1.0);
        if (section == 2)
        {
            lbTitle.text = @"特权";
        }
        else
        {
            lbTitle.text = @"其他";
        }
        
        [vBack addSubview:lbTitle];
        
        UIView* line = [[UIView alloc]initWithFrame:CGRectMake(0, 44, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
        line.backgroundColor = APP_CELL_LINE_COLOR;
        [vBack addSubview:line];
        
        [v addSubview:vBack];
        return v;
    }
    
    return nil;
}

- (void)btnAttentionClick
{
    if (isAttention)
    {
        [self cancelFollowShop:m_dicCardInfo];
    }
    else
    {
        [self followShop:m_dicCardInfo];
    }
}

- (void)cancelFollowShop:(NSDictionary*)dicBenefit
{
    if (dicBenefit == nil)
    {
        return;
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[dicBenefit objectForKey:@"shopCode"] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"cancelFollowShop" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"cancelFollowShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"取消关注成功"];
            isAttention = NO;
        }
        else
        {
            [self showAlertView:@"取消关注失败"];
        }
        
        [m_tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        [self showAlertView:@"取消关注失败"];
    }];
}

- (void)followShop:(NSDictionary*)dicBenefit
{
    if (dicBenefit == nil)
    {
        return;
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[dicBenefit objectForKey:@"shopCode"] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"followShop" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"followShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"关注成功"];
            isAttention = YES;
        }
        else
        {
            [self showAlertView:@"关注失败"];
        }
        
        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        [self showAlertView:@"关注失败"];
    }];
}


@end
