//
//  BMSQ_AwayOrderViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/1.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_AwayOrderViewController.h"
#import "ScreenView.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"
#import "OrderDetailViewControlelr.h"



#import "N_footTableViewCell.h"
#import "BMSQ_nOrderDetailViewController.h"
#import "CouponImage.h"
#import "NSDate+CZExtend.h"

@interface BMSQ_AwayOrderViewController ()<ScreenViewDelegate, UITableViewDataSource, UITableViewDelegate,UISearchBarDelegate>


@property (nonatomic, strong) ScreenView * screenView;
@property (nonatomic, strong) UITableView * baseView;

@property (nonatomic, strong)NSString *currentStatus;

@property (nonatomic, strong)UISearchBar *tx_search;

@property (nonatomic, strong)NSString *currntStatus;  //1 门面订单 2 外卖订单 3 非餐饮订单
@property (nonatomic, strong) NSMutableArray * dataSource;
@property (nonatomic,strong)NSMutableArray *foodArray;

@property (nonatomic, strong)NSDate *toDate;
@property (nonatomic, strong)NSDate *seleDate;

@property (nonatomic, strong)UIImageView *bgImage;

@property (nonatomic, strong)NSString  *currentPage;//当前页
@property (nonatomic, strong)NSString  *nextPage;//下一页


@end

@implementation BMSQ_AwayOrderViewController



- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    self.dataSource = [[NSMutableArray alloc]init];
    self.foodArray = [[NSMutableArray alloc]init];
    self.toDate = [NSDate date];
    self.seleDate = self.toDate;
    self.currentStatus = @"0";
    self.currentPage = @"1";
    self.nextPage = @"1";
    
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 110, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y- 110) ];
    self.baseView.backgroundColor = [UIColor clearColor];
    self.baseView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.baseView.dataSource = self;
    self.baseView.delegate = self;
    [self.view addSubview:self.baseView];
    
    self.bgImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 150, 150)];
    self.bgImage.center = CGPointMake(APP_VIEW_WIDTH/2, APP_VIEW_HEIGHT/2);
    [self.bgImage setImage:[UIImage imageNamed:@"iv_loadingLogo"]];
    self.bgImage.hidden = YES;
    [self.view addSubview:self.bgImage];
    

    [self addSearch];
    [self addSeleDayView];
    [self addSeleView];
    
    if ([self.orderTypeValue isEqualToNumber:[NSNumber numberWithInt:20]]) {
        [self setNavTitle:@"门店订单"];
        self.currntStatus = @"20";
        [self customRightBtn];
        
        [self getOrderB];
    }else if ([self.orderTypeValue isEqualToNumber:[NSNumber numberWithInt:21]]){
        [self setNavTitle:@"外卖订单"];
        self.currntStatus = @"21";
        [self customRightBtn];
        
        [self getOrderB];
        
    }else{
        [self setNavTitle:@"我的订单"];
        self.currntStatus = @"3";
        [self.baseView addFooterWithTarget:self action:@selector(freshTableView)];
        [self getOrderListForB];
    }
    
    
}
- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44);
    [item setTitle:@"筛选" forState:UIControlStateNormal];
    item.titleLabel.font = [UIFont systemFontOfSize:14.f];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemClick) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
}
-(void)addSearch{
    UIView *searchView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 50)];
    searchView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:searchView];
    
    
    self.tx_search = [[UISearchBar alloc]initWithFrame:CGRectMake(5, 10, APP_VIEW_WIDTH - 60, 30)];
    self.tx_search.delegate = self;
    self.tx_search.returnKeyType = UIReturnKeySearch;
    [self.tx_search setPlaceholder:@"请输入订单号/手机号/餐号"];
    self.tx_search.text = self.searchStr;
    
    
    [searchView addSubview:self.tx_search];
    UITextField *searchField = [self.tx_search valueForKey:@"_searchField"];
    searchField.backgroundColor = self.view.backgroundColor;
    searchField.font = [UIFont systemFontOfSize:13.f];
    
    UIButton *searchBtn = [[UIButton alloc]initWithFrame:CGRectMake(self.tx_search.frame.size.width, 0, 40, 50)];
    [searchBtn setTitle:@"查询" forState:UIControlStateNormal];
    [searchBtn setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
    searchBtn.titleLabel.font =[UIFont systemFontOfSize:14];
    [searchBtn addTarget:self action:@selector(clickSearchBtn) forControlEvents:UIControlEventTouchUpInside];
    [searchView addSubview:searchBtn];
    //设置背景图片
    float version = [[[ UIDevice currentDevice ] systemVersion ] floatValue ];
    
    if ([ self.tx_search respondsToSelector : @selector (barTintColor)]) {
        
        float  iosversion7_1 = 7.1 ;
        if (version >= iosversion7_1)
            
        {
            //iOS7.1
            [[[[ self.tx_search . subviews objectAtIndex : 0 ] subviews ] objectAtIndex : 0 ] removeFromSuperview ];
            [ self.tx_search setBackgroundColor :[ UIColor clearColor ]];
        }
        else
        {
            //iOS7.0
            [ self.tx_search setBarTintColor :[ UIColor clearColor ]];
            [ self.tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
    }
    else
    {
        //iOS7.0 以下
        [[ self.tx_search . subviews objectAtIndex : 0 ] removeFromSuperview ];
        [ self.tx_search setBackgroundColor :[ UIColor clearColor ]];
        
    }
    
}
-(void)addSeleDayView{

    UIView *searchView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+60, APP_VIEW_WIDTH, 45)];
    searchView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:searchView];

    UIButton *btn1 = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/3, searchView.frame.size.height)];
    [btn1 setTitle:@"上一天" forState:UIControlStateNormal];
    [btn1 setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
    btn1.titleLabel.font = [UIFont systemFontOfSize:14.f];
    btn1.tag = 10000;
    [searchView addSubview:btn1];
    
    UIButton *btn2 = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 0, APP_VIEW_WIDTH/3, searchView.frame.size.height)];
    [btn2 setTitle:@"今天" forState:UIControlStateNormal];
    [btn2 setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    btn2.titleLabel.font = [UIFont systemFontOfSize:14.f];
    btn2.tag = 10001;

    [searchView addSubview:btn2];
    
    UIButton *btn3 = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/3*2, 0, APP_VIEW_WIDTH/3, searchView.frame.size.height)];
    [btn3 setTitle:@"下一天" forState:UIControlStateNormal];
    [btn3 setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
    btn3.titleLabel.font = [UIFont systemFontOfSize:14.f];
    btn3.tag = 10002;

    [searchView addSubview:btn3];
    
    UIView *line1 = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, 10, 1, 25)];
    line1.backgroundColor = APP_TEXTCOLOR;
    line1.alpha = 0.6;
    [searchView addSubview:line1];
    UIView *line2 = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/3*2, 10, 1, 25)];
    line2.backgroundColor = APP_TEXTCOLOR;
    line2.alpha = 0.6;
    [searchView addSubview:line2];
    
    [btn1 addTarget:self action:@selector(clickDay:) forControlEvents:UIControlEventTouchUpInside];
    [btn2 addTarget:self action:@selector(clickDay:) forControlEvents:UIControlEventTouchUpInside];
    [btn3 addTarget:self action:@selector(clickDay:) forControlEvents:UIControlEventTouchUpInside];

    
    
}

#pragma mark 选择日期
-(void)clickDay:(UIButton *)btn{
    int tag = (int)btn.tag;
    for (int i= 10000; i<10003; i++) {
        UIButton *button = [self.view viewWithTag:i];
        if (i==tag) {
            [button setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
        }else{
            [button setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
   
        }
    }
    
    if(tag == 10000){  //上一天
        self.seleDate = [self.seleDate dateByAddingDays:-1];
    }else if (tag == 10001){//今天
        self.seleDate = self.toDate;
    }else if (tag == 10002){//下一天
        self.seleDate = [self.seleDate dateByAddingDays:1];

    }
    
    self.currentPage = @"1";
    self.nextPage = @"1";


    if ([self.currntStatus isEqualToString:@"3"]) { //非餐饮
        [self getOrderListForB];

    }else{
        [self getOrderB];
 
    }
}
-(void)addSeleView{
    self.screenView = [[ScreenView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    self.screenView.hidden = YES;
    self.screenView.scDelegate = self;
    [self.view addSubview:self.screenView];
}

#pragma mark 关键字查找事件
-(void)clickSearchBtn{
    [self.tx_search resignFirstResponder];
    if ([self.currntStatus isEqualToString:@"3"]) { //非餐饮
        [self getOrderListForB];
    }else{
        [self getOrderB];
    }
}
-(void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    [self.tx_search resignFirstResponder];
    if ([self.currntStatus isEqualToString:@"3"]) { //非餐饮
        [self getOrderListForB];
    }else{
        [self getOrderB];
    }
}
#pragma mark 非餐饮订单请求
-(void)getOrderListForB{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    if ( self.tx_search.text.length>0) {
        self.searchStr =self.tx_search.text;
    }else{
        self.searchStr =@"";
    }
    
    [params setObject:self.searchStr forKey:@"keyWard"];//关键词

    
    NSDateFormatter *formatter1 = [[NSDateFormatter alloc] init];
    [formatter1 setDateFormat:@"yyyy-MM-dd"];
    NSString *showtimeNew = [formatter1 stringFromDate:self.seleDate];
    [params setObject:showtimeNew forKey:@"date"];//查询字
    [params setObject:self.nextPage forKey:@"page"];//查询字
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getOrderListForB" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];

    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getOrderListForB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [weakSelf.dataSource addObjectsFromArray:[responseObject objectForKey:@"orderList"]];
        weakSelf.currentPage = weakSelf.nextPage;
        weakSelf.nextPage = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"nextPage"]];
        
        [weakSelf.baseView reloadData];
        
        if(weakSelf.dataSource.count ==0){
            weakSelf.bgImage.hidden = NO;
        }else{
            weakSelf.bgImage.hidden = YES;
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];

}

#pragma mark  查询门店或外卖订单列表
- (void)getOrderB
{
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getOrderB" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
   
    if ( self.tx_search.text.length>0) {
        self.searchStr =self.tx_search.text;
    }else{
        self.searchStr =@"";
    }
    
    [params setObject:self.searchStr forKey:@"keyWard"];//关键词
    [params setObject:self.currntStatus forKey:@"orderType"];//订单类型
    [params setObject:self.currentStatus forKey:@"status"];//状态
    [params setObject:@"D" forKey:@"unit"];//单位 为D
    
    NSDateFormatter *formatter1 = [[NSDateFormatter alloc] init];
    [formatter1 setDateFormat:@"yyyy-MM-dd"];
     NSString *showtimeNew = [formatter1 stringFromDate:self.seleDate];
     [params setObject:showtimeNew forKey:@"value"];//单位
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getOrderB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        weakSelf.foodArray = responseObject;
        [weakSelf.baseView reloadData];

        BOOL isHidden = NO;
        for (NSDictionary *dic in weakSelf.foodArray) {
            NSArray *orderList = [dic objectForKey:@"orderList"];
            if (orderList.count>0) {
                isHidden = YES;
                break;
            }
        }
        if(isHidden){
            weakSelf.bgImage.hidden = YES;
        }else{
            weakSelf.bgImage.hidden = NO;
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];

}


-(void)itemClick{
    self.screenView.hidden = !self.screenView.hidden;
}
-(void)clickBg{
    self.screenView.hidden = YES;
}
-(void)seleData:(NSString *)status{
    self.currentStatus = status;
    self.screenView.hidden = YES;

    [self getOrderB];
}
#pragma mark ---- UITableViewDataSource
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    if ([self.currntStatus isEqualToString:@"3"]) { //非餐饮
        return 1;
    }else{
        return self.foodArray.count;
    }
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if ([self.currntStatus isEqualToString:@"3"]) { //非餐饮
        return self.dataSource.count;
    }else{
        NSDictionary *dic= [self.foodArray objectAtIndex:section];
        NSString *countStr = [NSString stringWithFormat:@"%@",[dic objectForKey:@"count"]];
        return [countStr intValue];
    }
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if ([self.currntStatus isEqualToString:@"3"]) { //非餐饮
        return 106;

    }else{
        return 50;
    }
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if ([self.currntStatus isEqualToString:@"3"]) { //非餐饮
        static NSString * cell_id = @"no_OrderDetailsViewCell";
        N_footTableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[N_footTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        
        [cell setMyCell:[self.dataSource objectAtIndex:indexPath.row]];
        return cell;

    }else{
        
        static NSString * cell_id = @"OrderDetailsViewCell";
        OrderDetailsViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[OrderDetailsViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        cell.selectionStyle=UITableViewCellSelectionStyleNone;
        NSDictionary *dic = [self.foodArray objectAtIndex:indexPath.section];
        NSArray *orderList = [dic objectForKey:@"orderList"];
        NSDictionary *model = [orderList objectAtIndex:indexPath.row];
        
        NSString * mealNumStr = [model objectForKey:@"mealNbr"];
        cell.mealNumberLabel.text = [NSString stringWithFormat:@"餐号：%@", mealNumStr];
        cell.dataLabel.text = [model objectForKey:@"orderTime"];

        cell.stateLabel.text = [CouponImage getOrderStatus:[NSString stringWithFormat:@"%@",[model objectForKey:@"status"]]];
        return cell;
    }
}
#pragma mark ---- UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{

    if ([self.currntStatus isEqualToString:@"3"]){
        NSDictionary *dic = [self.dataSource objectAtIndex:indexPath.row];

        BMSQ_nOrderDetailViewController *vc = [[BMSQ_nOrderDetailViewController alloc]init];
        vc.consumeCode = [dic objectForKey:@"consumeCode"];
        [self.navigationController pushViewController:vc animated:YES];
        
    }else{
        NSDictionary *dic = [self.foodArray objectAtIndex:indexPath.section];
        NSArray *orderList = [dic objectForKey:@"orderList"];
        NSDictionary *model = [orderList objectAtIndex:indexPath.row];
        OrderDetailViewControlelr *vc = [[OrderDetailViewControlelr alloc] init];
        vc.orderCode = [model objectForKey:@"orderCode"];
        vc.eatPayType = [NSNumber numberWithInt:[[NSString stringWithFormat:@"%@",[model objectForKey:@"eatPayType"]]intValue]];
        [self.navigationController pushViewController:vc animated:YES];
    }
}
//返回区头的高度
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if ([self.currntStatus isEqualToString:@"3"]){
        return 0;
    }else{
        NSDictionary *dic = [self.foodArray objectAtIndex:section];
        NSArray *orderList = [dic objectForKey:@"orderList"];
        if (orderList.count>0) {
            return 30;
            
        }else{
            return 0;
        }
    }
}
//
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.1f;
}
//// 返回区头视图
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{

    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 30)];
    bgView.backgroundColor = UICOLOR( 239,  239, 244, 1);
    
    UILabel * statesLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 30)];
    NSDictionary *dic = [self.foodArray objectAtIndex:section];
    NSString *count =  [NSString stringWithFormat:@"%@",[dic objectForKey:@"count"]];
    statesLB.text = [NSString stringWithFormat:@"%@(%@)", [dic objectForKey:@"title"], count];
    statesLB.font = [UIFont systemFontOfSize:13];
    statesLB.textColor = APP_NAVCOLOR;
    [bgView addSubview:statesLB];
    return bgView;
}

-(void)freshTableView{
    if (![self.currentPage isEqualToString:self.nextPage]) {
        [self getOrderListForB];
    }
    
    [self.baseView footerEndRefreshing];

}
@end
