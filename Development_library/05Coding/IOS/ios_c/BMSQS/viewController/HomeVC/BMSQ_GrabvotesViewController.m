//
//  BMSQ_GrabvotesViewController.m
//  BMSQC
//
//  Created by liuqin on 15/12/8.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_GrabvotesViewController.h"

#import "BMSQ_CuponCell.h"
#import "MJRefresh.h"

#import <ShareSDK/ShareSDK.h>

#import "BMSQ_LoginViewController.h"
#import "BaseNavViewController.h"
#import "BMSQ_CouponDetailViewController.h"
#import "BMSQ_BuyCouponViewController.h"

#import "BMSQ_Share.h"

@interface BMSQ_GrabvotesViewController ()<UITableViewDataSource,UITableViewDelegate,cuponCellDelegate,UISearchBarDelegate>

@property (nonatomic, strong)UITableView *m_tableView;

@property (nonatomic, strong)NSMutableArray *m_dataSource;

@property (nonatomic, assign)int page;
@property (nonatomic, strong)UISearchBar *tx_search;

@property (nonatomic, strong)NSString *searchWord;

@end

@implementation BMSQ_GrabvotesViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.page = 1;
    self.searchWord = @"";
    [self setNavBackItem];
    self.m_dataSource = [[NSMutableArray alloc]init];
    
   UIImageView *iv_topTitle = [[UIImageView alloc]initWithFrame:CGRectMake(40, APP_STATUSBAR_HEIGHT + 6, APP_VIEW_WIDTH -88, 30)];
    iv_topTitle.backgroundColor = [UIColor clearColor];
    iv_topTitle.userInteractionEnabled = YES;
    
    self.tx_search = [[UISearchBar alloc]initWithFrame:CGRectMake(0, 0, iv_topTitle.frame.size.width, 30)];
    self.tx_search.delegate = self;
    [self.tx_search setPlaceholder:@"搜索商户名优惠"];
    self.tx_search.returnKeyType = UIReturnKeySearch;
    
    //设置背景图片
    float version = [[[ UIDevice currentDevice ] systemVersion ] floatValue ];
    
    if ([ self.tx_search respondsToSelector : @selector (barTintColor)]) {
        
        float  iosversion7_1 = 7.1 ;
        
        if (version >= iosversion7_1) {
            
            //iOS7.1
            
            [[[[ self.tx_search . subviews objectAtIndex : 0 ] subviews ] objectAtIndex : 0 ] removeFromSuperview ];
            
            [ self.tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
        else {
            
            //iOS7.0
            
            [ self.tx_search setBarTintColor :[ UIColor clearColor ]];
            
            [ self.tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
    }
    else {
        
        //iOS7.0 以下
        
        [[ self.tx_search . subviews objectAtIndex : 0 ] removeFromSuperview ];
        
        [ self.tx_search setBackgroundColor :[ UIColor clearColor ]];
        
    }
    
    
    
    [iv_topTitle addSubview:self.tx_search];
    [self setNavCustomerView:iv_topTitle];
    
    [self listCoupon];
    
    
    
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y) ];
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.m_tableView addFooterWithTarget:self action:@selector(footResh)];
    [self.view addSubview:self.m_tableView];
    


}

#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.m_dataSource.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
      NSDictionary *dic = [self.m_dataSource objectAtIndex:indexPath.row];
    
    if ([dic objectForKey:@"isSelect"]) {
        
        NSString *str = [dic objectForKey:@"isSelect"];
        if ([str isEqualToString:@"YES"]) {
            return 170;
            
        }else{
            return 100;
            
        }
        
    }else{
        return  100;
        
    }
    
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
        //操作区域
        static NSString *cellIdentifier = @"Cell";
        BMSQ_CuponCell *cell = (BMSQ_CuponCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil)
        {
            cell = [[BMSQ_CuponCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.backgroundColor = APP_VIEW_BACKCOLOR;
            cell.self.coupondelegate = self;
            cell.isGrabvotes = YES;
            cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        }
    //status 1  有效的
    
        [cell setCellValue:[self.m_dataSource objectAtIndex:indexPath.row] row:(int)indexPath.row couponStatus:1];
        return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if(![gloabFunction isLogin]){
        [self getLogin];
        
    }else{
        
        BMSQ_CuponCell *cell = (BMSQ_CuponCell *)[tableView cellForRowAtIndexPath:indexPath];
        NSDictionary *dic =cell.couponDic;
        BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
        couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
        couponVC.CouponMessage = cell.couponMessage;
        couponVC.CouponNbr = [NSString stringWithFormat:@"券码批次:%@",[dic objectForKey:@"batchNbr"]];
        [self.navigationController pushViewController:couponVC animated:YES];
    }
}


-(void)listCoupon{

    [SVProgressHUD showWithStatus:@"正在加载"];
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    NSString *city  = [userDefults objectForKey:SELECITY];

    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    [params setObject:city forKey:@"city"];
    [params setObject:self.searchWord forKey:@"searchWord"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[NSString stringWithFormat:@"%d",self.page] forKey:@"page"];
    [params setObject:@"0" forKey:@"couponType"];

    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"listCoupon" strParams:@"0"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    
    [self initJsonPrcClient:@"2"];
    [self.jsonPrcClient invokeMethod:@"listCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [wself.m_dataSource addObjectsFromArray:[responseObject objectForKey:@"couponList"]];
        [wself.m_tableView reloadData];
        [self.m_tableView footerEndRefreshing];
        wself.page = wself.page+1;

    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];


    }];

    
}
-(void)footResh{
    [self listCoupon];
}
#pragma mark 抢
-(void)grabCupon:(NSDictionary *)dicCupon currenRow:(int)row{
    
    if (![gloabFunction isLogin]) {
        UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
        BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
        BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
        
        [self presentViewController:nav animated:YES completion:nil];
        
        return;
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[dicCupon objectForKey:@"batchCouponCode"] forKey:@"batchCouponCode"];
    [params setObject:@"2" forKey:@"sharedLvl"];
    NSString* vcode = [gloabFunction getSign:@"grabCoupon" strParams:[dicCupon objectForKey:@"batchCouponCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;

    [self.jsonPrcClient invokeMethod:@"grabCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"抢券成功"];
            NSMutableDictionary *dic = [[NSMutableDictionary alloc]initWithDictionary:dicCupon];
            [dic setObject:[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"userCount"]] forKey:@"countMyActiveReceived"];
            [weakSelf.self.m_dataSource replaceObjectAtIndex:row withObject:dic];
            NSIndexPath *indexPath=[NSIndexPath indexPathForRow:row inSection:0];
           [weakSelf.m_tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
            
        }
        else
        {
            
            int i = [code intValue];
            
            switch (i) {
                case 80218:
                    [self showAlertView:@"优惠券开始使用日期不正确"];
                    break;
                case 80219:
                    [self showAlertView:@"优惠券已经被领取"];
                    break;
                case 80220:
                    [self showAlertView:@"优惠券已过期"];
                    break;
                case 80221:
                    [self showAlertView:@"优惠券已领走"];
                    break;
                case 80222:
                    [self showAlertView:@"您领用的数量已经达上限"];
                    break;
                case 80223:
                    [self showAlertView:@"优惠券不存在"];
                    break;
                    
                default:
                    [self showAlertView:@"优惠券状态不对，过段时间再领吧"];
                    
                    break;
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        [self showAlertView:@"抢券失败"];
    }];
    
 
}
#pragma mark 买券
-(void)grabBuyCoupon:(NSDictionary *)dicCoupon{
    if (![gloabFunction isLogin]) {
        UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
        BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
        BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
        
        [self presentViewController:nav animated:YES completion:nil];
        
        return;
    }
    
    BMSQ_BuyCouponViewController *vc = [[BMSQ_BuyCouponViewController alloc]init];
    vc.batchCouponCode = [dicCoupon objectForKey:@"batchCouponCode"];
    [self.navigationController pushViewController:vc animated:YES];
}
- (void)setCellSelect:(BOOL)isSelect indexPath:(int)row dic:(NSDictionary*)dicupon{
    
    NSMutableDictionary *dic = [[NSMutableDictionary alloc]initWithDictionary:dicupon];
    
    if (isSelect)
    {
        [dic setObject:@"YES" forKey:@"isSelect"];
    }
    else
    {
        [dic setObject:@"NO" forKey:@"isSelect"];
        
    }
    
        [self.m_dataSource replaceObjectAtIndex:row withObject:dic];
    
       [self.m_tableView reloadData];
    

}

- (void)btnShareClick:(NSDictionary*)dicShare{
    //创建分享内容
    
    NSNumber *couponType = [dicShare objectForKey:@"couponType"];
    
    NSString *str;
    
    if (couponType.intValue == 1)
    {
        str = [NSString stringWithFormat:@"%@",[dicShare objectForKey:@"function"]];
        
    } else if (couponType.intValue == 3) {
        str = [NSString stringWithFormat:@"满%@元立减%@元",[dicShare objectForKey:@"availablePrice"],[dicShare objectForKey:@"insteadPrice"]];
        
    }else if (couponType.intValue == 4){
        
        str = [NSString stringWithFormat:@"满%@元打%0.1f折",[dicShare objectForKey:@"availablePrice"],[[dicShare objectForKey:@"discountPercent"] floatValue]];
        
    }else{
        str = [NSString stringWithFormat:@"%@",[dicShare objectForKey:@"function"]];
        
    }
    
    NSString *city = [dicShare objectForKey:@"city"];
    NSString *title = [NSString stringWithFormat:@"【 %@ 】我分享你一张优惠券，手快有，手慢无",city];
    //    我分享你一张诺亚方舟电影院的优惠券，到惠圈，惠生活！
    NSString *shopName = [NSString stringWithFormat:@"%@", [dicShare objectForKey:@"shopName"]];
    
    NSString* remark = [NSString stringWithFormat:@"%@，我分享你一张%@的优惠券，到惠圈，惠生活！",[gloabFunction changeNullToBlank:str],shopName];
    
    NSString* url = [NSString stringWithFormat:@"%@/BatchCoupon/share?batchCouponCode=%@",BASE_URL,[dicShare objectForKey:@"batchCouponCode"]];
    NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"share" ofType:@"png"];
    [BMSQ_Share shareClick:remark imagePath:imagePath title:title url:url];
    
}


#pragma  mark uisearchbar delegate
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchbar
{
    [self.tx_search resignFirstResponder];
    [self.m_dataSource removeAllObjects];
    [self.m_tableView reloadData];
    self.page = 1;
    self.searchWord = searchbar.text;
    [self listCoupon];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) searchbar
{
    [self.tx_search resignFirstResponder];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.tx_search resignFirstResponder];
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    [self.m_dataSource removeAllObjects];
    self.page = 1;
    self.searchWord = searchText;
   [self listCoupon];
}

-(void)clickCell:(NSDictionary *)dic currenRow:(int)row{
    
    if (![gloabFunction isLogin]) {
        UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
        BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
        BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
        
        [self presentViewController:nav animated:YES completion:nil];
        
        return;
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[dic objectForKey:@"batchCouponCode"] forKey:@"batchCouponCode"];
    [params setObject:@"2" forKey:@"sharedLvl"];
    NSString* vcode = [gloabFunction getSign:@"grabCoupon" strParams:[dic objectForKey:@"batchCouponCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"grabCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"抢券成功"];
            NSMutableDictionary *newdic = [[NSMutableDictionary alloc]initWithDictionary:dic];
            [newdic setObject:[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"userCount"]] forKey:@"countMyActiveReceived"];
            [weakSelf.self.m_dataSource replaceObjectAtIndex:row withObject:newdic];
            NSIndexPath *indexPath=[NSIndexPath indexPathForRow:row inSection:0];
            [weakSelf.m_tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
            
        }
        else
        {
            
            int i = [code intValue];
            
            switch (i) {
                case 80218:
                    [self showAlertView:@"优惠券开始使用日期不正确"];
                    break;
                case 80219:
                    [self showAlertView:@"优惠券已经被领取"];
                    break;
                case 80220:
                    [self showAlertView:@"优惠券已过期"];
                    break;
                case 80221:
                    [self showAlertView:@"优惠券已领走"];
                    break;
                case 80222:
                    [self showAlertView:@"您领用的数量已经达上限"];
                    break;
                case 80223:
                    [self showAlertView:@"优惠券不存在"];
                    break;
                    
                default:
                    [self showAlertView:@"优惠券状态不对，过段时间再领吧"];
                    
                    break;
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        [self showAlertView:@"抢券失败"];
    }];
    

}
@end
