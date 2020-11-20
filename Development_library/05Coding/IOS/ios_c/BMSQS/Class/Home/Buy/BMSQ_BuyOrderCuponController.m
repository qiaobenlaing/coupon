//
//  BMSQ_BuyOrderCuponController.m
//  BMSQC
//
//  Created by djx on 15/8/8.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_BuyOrderCuponController.h"

@interface BMSQ_BuyOrderCuponController ()
{
    UITableView* m_tableView;
    NSMutableArray* m_dataSource;
}

@end

@implementation BMSQ_BuyOrderCuponController
@synthesize shopCode;
@synthesize consumeAmount;
@synthesize delegate;

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
    
    m_dataSource = [[NSMutableArray alloc]init];
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    [self listUserCoupon];
    
}

- (void)listUserCoupon
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:shopCode forKey:@"shopCode"];
    [params setObject:consumeAmount forKey:@"consumeAmount"];
    NSString* vcode = [gloabFunction getSign:@"listUserCoupon" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"listUserCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        m_dataSource = [[NSMutableArray alloc]initWithArray:responseObject];
        [ProgressManage closeProgress];
        
        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        
    }];
}


#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return m_dataSource.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 77;
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
    
    UIView* vBack = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 67)];
    [vBack setBackgroundColor:[UIColor clearColor]];
    
    UIImageView* v = [[UIImageView alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 67)];
    UIImage* iv = [UIImage imageNamed:@"iv_shopCupon"];
    iv = [iv stretchableImageWithLeftCapWidth:iv.size.width/2 topCapHeight:iv.size.height/2];
    v.image = iv;
    
    UILabel* lb_rmb = [[UILabel alloc]initWithFrame:CGRectMake(25, 15, 30, 30)];
    lb_rmb.text = RMB_SYMBOL;
    lb_rmb.textColor = [UIColor whiteColor];
    lb_rmb.font = [UIFont fontWithName:APP_FONT_NAME size:15];
    [v addSubview:lb_rmb];
    
    UILabel* lb_Price = [[UILabel alloc]initWithFrame:CGRectMake(45, 5, 100, 50)];
    lb_Price.text = [NSString stringWithFormat:@"%@",[[m_dataSource objectAtIndex:indexPath.row] objectForKey:@"insteadPrice"]];
    lb_Price.textColor = [UIColor whiteColor];
    lb_Price.font = [UIFont fontWithName:APP_FONT_NAME size:25];
    [v addSubview:lb_Price];
    
    UILabel* lb_maxPrice = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 230, 15, 200, 20)];
    lb_maxPrice.text = [NSString stringWithFormat:@"满%@元可以使用",[[m_dataSource objectAtIndex:indexPath.row] objectForKey:@"availablePrice"]];
    lb_maxPrice.textColor = [UIColor whiteColor];
    lb_maxPrice.textAlignment = NSTextAlignmentRight;
    lb_maxPrice.font = [UIFont systemFontOfSize:13];
    [v addSubview:lb_maxPrice];
    
    UILabel* lb_endTime = [[UILabel alloc]initWithFrame:CGRectMake(0, v.frame.size.height - 25, v.frame.size.width, 20)];
    lb_endTime.text = [NSString stringWithFormat:@"最后使用时间:%@",[[m_dataSource objectAtIndex:indexPath.row] objectForKey:@"expireTime"]];
    lb_endTime.textColor = [UIColor whiteColor];
    lb_endTime.textAlignment = NSTextAlignmentCenter;
    lb_endTime.font = [UIFont systemFontOfSize:13];
    [v addSubview:lb_endTime];
    
    [vBack addSubview:v];

    [cell addSubview:vBack];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (delegate != nil)
    {
        [delegate getCuponData:[m_dataSource objectAtIndex:indexPath.row]];
        [self.navigationController popViewControllerAnimated:YES];
    }
}

@end
