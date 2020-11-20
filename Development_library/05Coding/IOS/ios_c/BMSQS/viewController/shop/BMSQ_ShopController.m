//
//  BMSQ_ShopController.m
//  BMSQC
//
//  Created by djx on 15/7/29.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopController.h"
#import "BMSQ_ShopDetailController.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"
#import "BMSQ_ShopDetailView.h"

#import "BMSQ_RecommendShopCell.h"
#import "BMSQ_NewShopDetailViewController.h"
#import "ShopListTopView.h"
#import "ShopListDataView.h"
#import "BMSQ_LoginViewController.h"
#import "BaseNavViewController.h"
#import "MobClick.h"
@interface BMSQ_ShopController ()<ShopListTopViewDelegate,ShopListDataViewDelegate>
{
   
    int pageNumber; //当前页码
    UISearchBar* tx_search;
    UIImageView* iv_topTitle;

}


@property (nonatomic, strong)UITableView *m_tableView;
@property (nonatomic,assign)BOOL isHave;
@property (nonatomic, strong) NSMutableArray* m_dataSource;

@property(nonatomic, strong)UIButton *alphView;
@property(nonatomic, strong)ShopListTopView *topSeleView;
@property(nonatomic, strong)ShopListDataView *listDataView;

@property (nonatomic, strong)NSDictionary *circleDic;  //商圈数据
@property (nonatomic, strong)NSDictionary *intelligentSortingDic; //智能排序数据
@property (nonatomic, strong)NSDictionary *typeDic; //行业
@property (nonatomic, strong)NSDictionary *filterDic; //筛选

@property (nonatomic, assign)int seleTopTag;

@property (nonatomic, assign)BOOL isreqShop;
@property (nonatomic, assign)BOOL isreqSele;    //SVProgressHUD

@property (nonatomic, strong)NSMutableDictionary *paramDic;

@property(nonatomic, strong)UIImageView *nodataImage;

@end

@implementation BMSQ_ShopController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"ShopController"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"ShopController"];
}


/////要登录?????
- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.isreqSele = NO;
    self.isreqShop = NO;
    
    pageNumber = 1;
    
    self.paramDic = [[NSMutableDictionary alloc]init];
    [self.paramDic setObject:self.moduleValue.length>0?self.moduleValue:@"0" forKey:@"moduleValue"];
    [self.paramDic setObject:self.content.length>0?self.content:@"0" forKey:@"content"];
    [self.paramDic setObject:[NSString stringWithFormat:@"%d",self.typeInt] forKey:@"type"];
    [self.paramDic setObject:@"0" forKey:@"filter"];
    [self.paramDic setObject:@"0" forKey:@"order"];
    
    
    self.topSeleView = [[ShopListTopView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 40)];
    self.topSeleView.listDelegate = self;
    [self.view addSubview:self.topSeleView];
    if (self.isSeleType == 1) {
        [self.topSeleView changeTitle:self.typeStr tag:1001];
    }else if(self.isSeleType == 2){
        [self.topSeleView changeTitle:self.typeStr tag:1000];

    }

    
    self.nodataImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+40, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    [self.nodataImage setImage:[UIImage imageNamed:@"noDataImage"]];
    [self.view addSubview:self.nodataImage];
    
    [SVProgressHUD showWithStatus:@"" maskType:SVProgressHUDMaskTypeClear];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(dismissSVProgressHUD) name:@"dismissSVProgressHUD" object:nil];
    self.seleTopTag = 0;
    
 
    [self setViewUp];
    


}

-(void)dismissSVProgressHUD{
    
    if (self.isreqShop && self.isreqSele) {
        [SVProgressHUD dismiss];
    }
    
}
-(void)seleCircleCell:(NSDictionary *)dic title:(NSString *)title seleRow:(int)row seleSecRow:(int)secrow{
    
    self.alphView.hidden = YES;
    self.listDataView.hidden = YES;
    if(secrow == 999999){
        NSArray *array = [dic objectForKey:@"list"];
        NSDictionary *listDic = [array objectAtIndex:row];
        self.moduleValue = [listDic objectForKey:@"moduleValue"];
        self.content = [listDic objectForKey:@"queryName"];
        [self.paramDic setObject:self.moduleValue forKey:@"moduleValue"];
        [self.paramDic setObject:self.content forKey:@"content"];
        pageNumber = 1;
        [self.m_dataSource removeAllObjects];
        [self searchShop:self.paramDic];
        [self.topSeleView changeTitle:title tag:self.seleTopTag];
        
    }else{
        NSArray *array = [dic objectForKey:@"list"];
        NSDictionary *listDic = [array objectAtIndex:row];
        NSArray *subList = [listDic objectForKey:@"subList"];
        NSDictionary *subDic = [subList objectAtIndex:secrow];
        self.moduleValue = [subDic objectForKey:@"moduleValue"];
        self.content = [subDic objectForKey:@"value"];
        
        [self.paramDic setObject:self.moduleValue forKey:@"moduleValue"];
        [self.paramDic setObject:self.content forKey:@"content"];
        pageNumber = 1;
        [self.m_dataSource removeAllObjects];
        [self searchShop:self.paramDic];
        [self.topSeleView changeTitle:title tag:self.seleTopTag];
    }
    
}
-(void)seleCell:(NSDictionary *)dic title:(NSString *)title seleRow:(int)row{
    self.alphView.hidden = YES;
    self.listDataView.hidden = YES;
    [self.topSeleView changeTitle:title tag:self.seleTopTag];
    if (self.seleTopTag == 1001) { //行业
        NSArray *array = [dic objectForKey:@"list"];
        NSDictionary *listDic = [array objectAtIndex:row];
        NSString *type = [NSString stringWithFormat:@"%@",[listDic objectForKey:@"value"]];
        self.typeInt = [type intValue];
        pageNumber = 1;
        [self.m_dataSource removeAllObjects];
        [self searchShop:self.paramDic];
    }else if(self.seleTopTag == 1003){  //筛选
        NSArray *array = [dic objectForKey:@"list"];
        NSDictionary *listDic = [array objectAtIndex:row];
        NSString *type = [NSString stringWithFormat:@"%@",[listDic objectForKey:@"value"]];
        [self.paramDic setObject:type forKey:@"filter"];
        pageNumber = 1;
        [self.m_dataSource removeAllObjects];
        [self searchShop:self.paramDic];
    }else if(self.seleTopTag == 1002){  //智能排序
        NSArray *array = [dic objectForKey:@"list"];
        NSDictionary *listDic = [array objectAtIndex:row];
        NSString *type = [NSString stringWithFormat:@"%@",[listDic objectForKey:@"value"]];
        [self.paramDic setObject:type forKey:@"order"];
        pageNumber = 1;
        [self.m_dataSource removeAllObjects];
        [self searchShop:self.paramDic];
    }
    
}
-(void)showListDataDelegate:(int)i{
    
   self.alphView.hidden = NO;
    
    if (self.listDataView ==nil) {
        self.listDataView  = [[ShopListDataView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+40, APP_VIEW_WIDTH, 0)];
        self.listDataView.backgroundColor = [UIColor clearColor];
        self.listDataView.seleDelegate = self;
        [self.view addSubview:self.listDataView];
    }else{
        self.listDataView.hidden = NO;
 
    }
//    行业 11  智能排序 22  筛选 33
    if (i == 1000) {   //商圈
        self.seleTopTag = 1000;
        
        [UIView animateWithDuration:0.1 animations:^{
            self.listDataView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y+40, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/2);
            
            [self.listDataView circleTable:self.circleDic];
        }];
    }else if (i== 1001){   //行业
        self.seleTopTag = 1001;
        NSArray *array;
        if ([self.typeDic objectForKey:@"list"]) {
            array = [self.typeDic objectForKey:@"list"];
        }
        float height = array.count*30>APP_VIEW_HEIGHT/2?APP_VIEW_HEIGHT/2:array.count*30;
        [UIView animateWithDuration:0.1 animations:^{
            self.listDataView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y+40, APP_VIEW_WIDTH,  height);
            [self.listDataView onlyTable:self.typeDic tag:11];
        }];
    }else if (i== 1002){   //智能排序
        self.seleTopTag = 1002;
        NSArray *array;
        if ([self.intelligentSortingDic objectForKey:@"list"]) {
            array = [self.intelligentSortingDic objectForKey:@"list"];
        }
        float height = array.count*30>APP_VIEW_HEIGHT/2?APP_VIEW_HEIGHT/2:array.count*30;

        [UIView animateWithDuration:0.1 animations:^{
            self.listDataView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y+40, APP_VIEW_WIDTH,  height);
            [self.listDataView onlyTable:self.intelligentSortingDic tag:22];
        }];
    }else if (i== 1003){   //筛选
        self.seleTopTag = 1003;
        NSArray *array;
        if ([self.filterDic objectForKey:@"list"]) {
            array = [self.filterDic objectForKey:@"list"];
        }
        float height = array.count*30>APP_VIEW_HEIGHT/2?APP_VIEW_HEIGHT/2:array.count*30;
        
        [UIView animateWithDuration:0.1 animations:^{
            self.listDataView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y+40, APP_VIEW_WIDTH, height);
            [self.listDataView onlyTable:self.filterDic tag:33];
        }];
    }

    
}

#pragma mark ---
- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavBackItem];

    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 40, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT - 40)];
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.backgroundColor = [UIColor clearColor];
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.m_tableView addFooterWithTarget:self action:@selector(footfresh)];
    [self.view addSubview:self.m_tableView];
    
    iv_topTitle = [[UIImageView alloc]initWithFrame:CGRectMake(30, APP_STATUSBAR_HEIGHT + 6, APP_VIEW_WIDTH - 60, 30)];
    iv_topTitle.userInteractionEnabled = YES;
    tx_search = [[UISearchBar alloc]initWithFrame:CGRectMake(5, 0, APP_VIEW_WIDTH - 70, 30)];
    tx_search.delegate = self;
    tx_search.returnKeyType = UIReturnKeySearch;
    [tx_search setPlaceholder:@"搜索商户名、地点"];
    if (_kbShow) {
        [tx_search becomeFirstResponder];
        
    }

     //设置背景图片
    float version = [[[ UIDevice currentDevice ] systemVersion ] floatValue ];
    
    if ([ tx_search respondsToSelector : @selector (barTintColor)]) {
        
        float  iosversion7_1 = 7.1 ;
        
        if (version >= iosversion7_1)
            
        {
            
            //iOS7.1
            
            [[[[ tx_search . subviews objectAtIndex : 0 ] subviews ] objectAtIndex : 0 ] removeFromSuperview ];
            
            [ tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
        else
            
        {
            
            //iOS7.0
            
            [ tx_search setBarTintColor :[ UIColor clearColor ]];
            
            [ tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
    }
    else
    {
        
        //iOS7.0 以下
        
        [[ tx_search . subviews objectAtIndex : 0 ] removeFromSuperview ];
        
        [ tx_search setBackgroundColor :[ UIColor clearColor ]];
        
    }
    
    [iv_topTitle addSubview:tx_search];
    [self setNavCustomerView:iv_topTitle];
    
    pageNumber = 1;
    self.m_dataSource = [[NSMutableArray alloc]init];
    
    [self searchShop:self.paramDic];        //
    [self listSearchWords];   //加载选择条件
    
    
    self.alphView= [[UIButton alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + self.topSeleView.frame.size.height, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT - 40)];
    self.alphView.backgroundColor = [UIColor blackColor];
    self.alphView.alpha = 0.4;
    self.alphView.hidden = YES;

    [self.alphView addTarget:self action:@selector(hiddenAlphView) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:self.alphView];
}

#pragma mark ---- 上拉加载更多
-(void)footfresh{

    [self searchShop:self.paramDic];

}


/*
 *商圈：moduleValue + content
 *智能排序:order
 *筛选：filter
 *行业：type
 */

#pragma mark --Search
- (void)searchShop:(NSDictionary *)params
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* paramsDic = [[NSMutableDictionary alloc]initWithDictionary:params];
    
    if (tx_search.text == nil || tx_search.text.length <= 0)
    {
        [paramsDic setObject:@"" forKey:@"searchWord"];
        
    }
    else
    {
        [paramsDic setObject:tx_search.text forKey:@"searchWord"];
        
    }

    [paramsDic setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [paramsDic setObject:[NSString stringWithFormat:@"%d",pageNumber] forKey:@"page"];   ///////test
    [paramsDic setObject:[NSString stringWithFormat:@"%d",self.typeInt] forKey:@"type"];
    

    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    NSString *cityName = [userDefults objectForKey:SELECITY];
    [paramsDic setObject:longitude forKey:@"longitude"];
    [paramsDic setObject:latitude forKey:@"latitude"];
    [paramsDic setObject:cityName forKey:@"city"];


    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"searchShop" withParameters:paramsDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        wself.isreqShop = YES;
        
        [[NSNotificationCenter defaultCenter]postNotificationName:@"dismissSVProgressHUD" object:nil];;
        
        NSArray *resultArray = [responseObject objectForKey:@"shopList"];
        [wself.m_dataSource addObjectsFromArray:[responseObject objectForKey:@"shopList"]];

        if (resultArray.count>0) {
            pageNumber = pageNumber + 1;

        }else{
            
        }

        [wself.m_tableView footerEndRefreshing];

        [wself.m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
    }];
}

#pragma mark --选择条件--
- (void)listSearchWords{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *cityName = [userDefaults objectForKey:SELECITY];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    [params setObject:cityName forKey:@"city"];
    
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"listSearchWords" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        wself.isreqSele = YES;
        [[NSNotificationCenter defaultCenter]postNotificationName:@"dismissSVProgressHUD" object:nil];;

        if ([responseObject objectForKey:@"circle"]) {
            NSDictionary *circle = [responseObject objectForKey:@"circle"];
            wself.circleDic = circle;
            
        }
        
        if ([responseObject objectForKey:@"type"]) {
            NSDictionary *type = [responseObject objectForKey:@"type"];
            wself.typeDic = type;

        }
        
        
        if ([responseObject objectForKey:@"filter"]) {
            NSDictionary *filter = [responseObject objectForKey:@"filter"];
            wself.filterDic = filter;
        }
        if ([responseObject objectForKey:@"intelligentSorting"]) {
            NSDictionary *intelligentSorting = [responseObject objectForKey:@"intelligentSorting"];
            wself.intelligentSortingDic = intelligentSorting;
        }

        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
    
}





#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    

        if(self.m_dataSource.count>0){
            self.nodataImage.hidden = YES;
        }else{
            self.nodataImage.hidden = NO;

        }
        
        return self.m_dataSource.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return [BMSQ_RecommendShopCell cellHeigh:[self.m_dataSource objectAtIndex:indexPath.row]];

}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
        //操作区域
        static NSString *cellIdentifier = @"BMSQ_ShopCellcell";
        BMSQ_RecommendShopCell *cell = (BMSQ_RecommendShopCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            cell = [[BMSQ_RecommendShopCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        }
        [cell setCellValue:[self.m_dataSource objectAtIndex:indexPath.row]];
        
        return cell;

}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if(![gloabFunction isLogin]){
        [self getLogin];
    }else{
        
//        NSDictionary* dicData = [self.m_dataSource objectAtIndex:indexPath.row];
//        BMSQ_ShopDetailController* detailCtrl = [[BMSQ_ShopDetailController alloc]init];
//        detailCtrl.shopCode = [dicData objectForKey:@"shopCode"];
//        detailCtrl.shopName = [dicData objectForKey:@"shopName"];
//        detailCtrl.shopImage = [dicData objectForKey:@"logoUrl"];
//        detailCtrl.couponDic = dicData;
//        [self.navigationController pushViewController:detailCtrl animated:YES];
        
        BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
        NSDictionary* dicData = [self.m_dataSource objectAtIndex:indexPath.row];
        detailCtrl.shopCode = [dicData objectForKey:@"shopCode"];
        detailCtrl.userCode = [gloabFunction getUserCode];
        detailCtrl.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:detailCtrl animated:YES];
        
        
        
        
    }

}

-(void)gotoDetail:(NSDictionary *)dic{
    BMSQ_ShopDetailController* detailCtrl = [[BMSQ_ShopDetailController alloc]init];
    detailCtrl.shopCode = [dic objectForKey:@"shopCode"];
    detailCtrl.shopName = [dic objectForKey:@"shopName"];
    detailCtrl.shopImage = [dic objectForKey:@"logoUrl"];
    [self.navigationController pushViewController:detailCtrl animated:YES];

}


#pragma  mark uisearchbar delegate
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchbar
{
    [tx_search resignFirstResponder];
    [self.m_dataSource removeAllObjects];
    [self.m_tableView reloadData];
    pageNumber = 1;
    [self searchShop:self.paramDic];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) searchbar
{
    [tx_search resignFirstResponder];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [tx_search resignFirstResponder];
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
//    [tx_search resignFirstResponder];
//    if (searchText.length==0) {
//        [self.m_dataSource removeAllObjects];
//        [self.m_tableView reloadData];
//        pageNumber = 1;
//        [self searchShop:self.paramDic];
//    }
  
}

//- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
//    
//}
//#pragma mark - 隐藏键盘
//- (void)dismissKeyB {
//    [tx_search resignFirstResponder];
//}

-(void)hiddenAlphView{
    self.alphView.hidden = YES;
    self.listDataView.hidden = YES;
    
}
@end
