//
//  BMSQ_CouponListViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/13.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponListViewController.h"


#import "BMSQ_CouponDetailViewController.h"
#import "BMSQ_CouponReceiveViewController.h"

#import "Coupon_ListTableViewCell.h"

#import "SVProgressHUD.h"

#import <ShareSDK/ShareSDK.h>
#import "HQShareUtils.h"

@interface BMSQ_CouponListViewController ()<UITableViewDataSource,UITableViewDelegate,Coupon_ListTableViewCellDelegate>

@property (nonatomic, strong)NSMutableArray *dataArray;
@property (nonatomic, strong)UITableView *dataTable;
@property (nonatomic, strong)UIImageView *m_noDataView;
@property (nonatomic, assign)int page;

@end

@implementation BMSQ_CouponListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self setNavBackItem];
    [self setNavTitle:@"我发布的优惠券"];
    self.page = 1;
    
    
    self.dataArray = [[NSMutableArray alloc]init];
    
    self.dataTable = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.dataTable.backgroundColor = [UIColor clearColor];
    self.dataTable.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.dataTable.dataSource =self;
    self.dataTable.delegate =self;
    [self.view addSubview:self.dataTable];
    
    [self.dataTable addFooterWithTarget:self action:@selector(footRefresh)];
    
    
    self.m_noDataView = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    self.m_noDataView.hidden = YES;
    self.m_noDataView.backgroundColor = [UIColor clearColor];;
    self.m_noDataView.image = [UIImage imageNamed:@"iv_noMessage"];
    
    
    [self.view addSubview:self.m_noDataView];
    [self.view bringSubviewToFront:self.m_noDataView];
    
    [self listShopCoupon];
}

-(void)footRefresh{
    [self.dataTable footerEndRefreshing];
    [self listShopCoupon];
    
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [SVProgressHUD dismiss];
}

#pragma mark UITableViewDelegate && UITableViewDataSource
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return APP_VIEW_HEIGHT/6+10;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"Coupon_ListTableViewCell";
    Coupon_ListTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[Coupon_ListTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.listDelegate = self;
    }
    [cell setListCoupon:[self.dataArray objectAtIndex:indexPath.row]];
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    BMSQ_CouponDetailViewController *vc = [[BMSQ_CouponDetailViewController alloc]init];
    NSDictionary *dic = [self.dataArray objectAtIndex:indexPath.row];
    vc.batchCouponCode = [dic objectForKey:@"batchCouponCode"];
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}


#pragma mark -- ListCouponDelegate
-(void)shareCoupon:(NSDictionary *)dic{
    NSLog(@"--------- >发布优惠券分享");
    
    
    NSNumber *couponType = [dic objectForKey:@"couponType"];
    
    NSString *str;
    
    if (couponType.intValue == 1)
    {
        str = [NSString stringWithFormat:@"%@",[dic objectForKey:@"function"]];
        
    } else if (couponType.intValue == 3) {
        str = [NSString stringWithFormat:@"满%@元立减%@元",[dic objectForKey:@"availablePrice"],[dic objectForKey:@"insteadPrice"]];
        
    }else if (couponType.intValue == 4){
        
        str = [NSString stringWithFormat:@"满%@元打%0.1f折",[dic objectForKey:@"availablePrice"],[[dic objectForKey:@"discountPercent"] floatValue]];
        
    }else{
        str = [NSString stringWithFormat:@"%@",[dic objectForKey:@"function"]];
        
    }
    
    NSString *city   = [dic objectForKey:@"city"];
    NSString *title = [NSString stringWithFormat:@"【 %@ 】我分享你一张优惠券，手快有，手慢无",city];
    //    //    我分享你一张诺亚方舟电影院的优惠券，到惠圈，惠生活！
    NSString *shopName = [NSString stringWithFormat:@"%@", [dic objectForKey:@"shopName"]];
    
    NSString* remark = [NSString stringWithFormat:@"%@我分享你一张的优惠券，到惠圈，惠生活！%@",[gloabFunction changeNullToBlank:str],shopName];
    
    NSString* url = [NSString stringWithFormat:@"%@/BatchCoupon/share?batchCouponCode=%@",BASE_URL,[dic objectForKey:@"batchCouponCode"]];
    NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"share" ofType:@"png"];
    
    [HQShareUtils shareCouponWithTitle:title content:remark url:url];
    
    
    

}
-(void)receiveCoupon:(NSDictionary *)dic{
    
    BMSQ_CouponReceiveViewController *vc = [[BMSQ_CouponReceiveViewController alloc]init];
    vc.batchCouponCode = [dic objectForKey:@"batchCouponCode"];
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark -- Request--

-(void)listShopCoupon{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSString stringWithFormat:@"1"] forKey:@"time"];
    [params setObject:[NSString stringWithFormat:@"%d",self.page] forKey:@"page"];

    NSString* vcode = [gloabFunction getSign:@"listShopCoupon" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"listShopCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [weakSelf.dataArray  addObjectsFromArray:[responseObject objectForKey:@"couponList"]];
        [weakSelf.dataTable reloadData];
        [SVProgressHUD dismiss];
        weakSelf.page =weakSelf.page +1;
        
        
        if(weakSelf.dataArray.count==0){
           
            weakSelf.m_noDataView.hidden = NO;

            
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];

    
    
}

-(void)rightButtonClicked{
    
}


@end
