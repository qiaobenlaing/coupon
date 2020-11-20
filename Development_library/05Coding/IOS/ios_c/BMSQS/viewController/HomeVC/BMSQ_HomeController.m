//
//  BMSQ_HomeController.m
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_HomeController.h"


#import "BMSQ_LoginViewController.h"
#import "BMSQ_ShopController.h"
#import "BMSQ_ShopDetailController.h"
#import "BMSQ_MyWebViewController.h"
#import "BMSQ_RecommendShopCell.h"
#import "BMSQ_homeShopZeroRowCell.h"

#import "BMSQ_adCell.h"
#import "BMSQ_homeTypeCell.h"
#import "BMSQ_homeCircleSquareTableViewCell.h"
#import "BMSQ_homeBrandTableViewCell.h"
#import "BMSQ_homeWebController.h"
#import "BMSQ_GrabvotesViewController.h"
#import "BMSQ_IcbcShopViewController.h"
#import "BMSQ_NewShopDetailViewController.h"
#import "BMSQ_educationViewController.h"
#import "MJRefresh.h"
#import "MobClick.h"
#define CITYTABLETAG 100  //城市TableView
#define MAINTABLETAG 200  //首页TableView



//AD高度
#define ADCELLH [[UIScreen mainScreen]bounds].size.height/4
//类型高度
#define TYPECELLH [[UIScreen mainScreen]bounds].size.height/7+10
//商圈高度
#define CIRCLECELLH [[UIScreen mainScreen]bounds].size.height/6
//品牌 活动 功能 高度
#define OTHERCELLH [[UIScreen mainScreen]bounds].size.height/7


@interface BMSQ_HomeController ()<BMSQ_adCellDelegate,BMSQ_homeTypeCellDelegate,BMSQ_homeCircleSquareTableViewCellDelegate,BMSQ_homeBrandTableViewCellDelegate,BMSQ_homeShopZeroRowCellDelegate>
{
    
    UISearchBar     *tx_search;         //搜索
    UIImageView     *iv_topTitle;       //顶部背景图
    UIButton        *btnCity;           //选择城市
    int             pageNumber;         //当前页码
//    BOOL            refreshing;         //刷新  YES：下拉 NO：上拉
    
    //首页新人注册通过api隐藏显示
    UIButton        *btn_float;
    NSNumber        *num_float;
    
    //扫描二维码用到的
    BOOL            upOrdown;
    NSTimer         *timer;
    UIImageView     *_line;
    int num;
}

@property (nonatomic,strong)UITableView *m_tableViewCity;
@property (nonatomic,strong)UITableView *m_tableView;

@property (nonatomic, strong)NSMutableArray *m_dataSource;
@property (nonatomic, strong)NSMutableArray *m_dataSourceShop;


@property (nonatomic, strong)NSDictionary *adDic;   //广告区域
@property (nonatomic, strong)NSDictionary *typeDic; //标签
@property (nonatomic, strong)NSDictionary *cicleDic;//圈广场
@property (nonatomic, strong)NSDictionary *brandDic;//品牌
@property (nonatomic, strong)NSDictionary *actidDic;//活动
@property (nonatomic, strong)NSDictionary *shopdDic;//活动
@property (nonatomic, strong)NSDictionary *functionDic;//功能


@property (nonatomic, assign)BOOL isSeleType; ////////


@property (nonatomic, assign)int rowCount;


@end

@implementation BMSQ_HomeController



- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.m_dataSource = [[NSMutableArray alloc]init];
    self.m_dataSourceShop = [[NSMutableArray alloc]init];
    self.rowCount = 0;

    [self addMainTableView];
    [self setNavigationBar];
    [self addNewRerButton];
    [self addCityTable];
    [self addNavView];
    pageNumber = 1;
    
    [self getHomeInfo];
    [self recordUserAddress];

    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(getHomeShopList) name:@"shoplist" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(getOpenListCity:) name:@"getOpenListCity" object:nil];

    
}
-(void)getOpenListCity:(NSNotification *)noti{
    NSArray *array = noti.object;
    self.m_dataSourceCity =array;
    [self.m_tableViewCity reloadData];
}
#pragma mark --主TableView--
- (void)addMainTableView{
    
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-49)];
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.tag = MAINTABLETAG;
    self.m_tableView.backgroundColor = UICOLOR(221, 221, 221, 1);
    [self.m_tableView addFooterWithTarget:self action:@selector(footRefresh)];
    [self.m_tableView addHeaderWithTarget:self action:@selector(headRefresh)];
    [self.view addSubview:self.m_tableView];
    
 }
#pragma mark --新人注册--
-(void)addNewRerButton{
    btn_float = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 100, 100, 30)];
    btn_float.backgroundColor = [UIColor clearColor];
    [btn_float setImage:[UIImage imageNamed:@"NewRer"] forState:UIControlStateNormal];
    [self.view addSubview:btn_float];
    [self.view bringSubviewToFront:btn_float];
    btn_float.hidden = YES;
    [btn_float addTarget:self action:@selector(newRes) forControlEvents:UIControlEventTouchUpInside];

}
-(void)newRes{
    
    BMSQ_MyWebViewController *vc = [[BMSQ_MyWebViewController alloc]init];
    vc.hidesBottomBarWhenPushed = YES;
    vc.requestStr= [NSString stringWithFormat:@"%@Browser/regAct",H5_URL];
    UINavigationController *na = [[UINavigationController alloc]initWithRootViewController:vc];
    [self presentViewController:na animated:YES completion:nil ];
    
}
- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    [MobClick beginLogPageView:@"HomePage"];
    [self getSystemParam];  //新人注册按扭
    
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"HomePage"];
}

//首页新人注册通过api隐藏显示
- (void)getSystemParam{
    [self initJsonPrcClient:@"2"];
    [self.jsonPrcClient invokeMethod:@"getSystemParam" withParameters:nil success:^(AFHTTPRequestOperation *operation, NSDictionary* responseObject) {
        
        if (responseObject != nil && responseObject.count > 0)
        {
            num_float = [responseObject objectForKey:@"isOpenRegAct"];
            
            if ([num_float intValue] == 1) {
                btn_float.hidden = NO;
            }else {
                btn_float.hidden = YES;
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
    
    
}
#pragma mark --导航栏 NAVView--
-(void)addNavView{
    
    NSUserDefaults *userdefaults = [NSUserDefaults standardUserDefaults];
    NSString *city =[userdefaults objectForKey:SELECITY];
    btnCity = [[UIButton alloc]initWithFrame:CGRectMake(0, 20, 65, 44)];
    btnCity.backgroundColor = [UIColor clearColor];
    UIImageView* ivSelectCity = [[UIImageView alloc]initWithFrame:CGRectMake(55, 17, 10, 10)];
    ivSelectCity.image = [UIImage imageNamed:@"iv_citySelect"];
    [btnCity addSubview:ivSelectCity];
    [btnCity setTitle:[gloabFunction changeNullToBlank:city] forState:UIControlStateNormal];
    btnCity.contentHorizontalAlignment = UIControlContentHorizontalAlignmentCenter;
    btnCity.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    [btnCity setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    
    [btnCity setTitle:city forState:UIControlStateNormal];
    [btnCity addTarget:self action:@selector(btnCityClick) forControlEvents:UIControlEventTouchUpInside];
    [btnCity.titleLabel setFont:[UIFont systemFontOfSize:13]];
    [self setNavLeftBarItem:btnCity];
    
    
    UIButton* btnRightBar = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44)];
    btnRightBar.backgroundColor = [UIColor clearColor];
    [btnRightBar addTarget:self action:@selector(btnRightClick) forControlEvents:UIControlEventTouchUpInside];
    [btnRightBar setImage:[UIImage imageNamed:@"iv_scan"] forState:UIControlStateNormal];
    
    [self setNavRightBarItem:btnRightBar];
    
    iv_topTitle = [[UIImageView alloc]initWithFrame:CGRectMake(btnCity.frame.size.width, APP_STATUSBAR_HEIGHT + 6, APP_VIEW_WIDTH -btnCity.frame.size.width - 44, 30)];
    iv_topTitle.backgroundColor = [UIColor clearColor];
    iv_topTitle.userInteractionEnabled = YES;
    
    tx_search = [[UISearchBar alloc]initWithFrame:CGRectMake(0, 0, iv_topTitle.frame.size.width, 30)];
    tx_search.delegate = self;
    [tx_search setPlaceholder:@"搜索商户名、地点"];
    UIButton* btnSearch = [[UIButton alloc]initWithFrame:CGRectMake(0, 0,iv_topTitle.frame.size.width, 30)];
    [btnSearch setBackgroundColor:[UIColor clearColor]];
    [btnSearch addTarget:self action:@selector(btnSearchClick) forControlEvents:UIControlEventTouchUpInside];
    [tx_search addSubview:btnSearch];
    
    //设置背景图片
    float version = [[[ UIDevice currentDevice ] systemVersion ] floatValue ];
    
    if ([ tx_search respondsToSelector : @selector (barTintColor)]) {
        
        float  iosversion7_1 = 7.1 ;
        
        if (version >= iosversion7_1) {
            
            //iOS7.1
            
            [[[[ tx_search . subviews objectAtIndex : 0 ] subviews ] objectAtIndex : 0 ] removeFromSuperview ];
            
            [ tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
        else {
            
            //iOS7.0
            
            [ tx_search setBarTintColor :[ UIColor clearColor ]];
            
            [ tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
    }
    else {
        
        //iOS7.0 以下
        
        [[ tx_search . subviews objectAtIndex : 0 ] removeFromSuperview ];
        
        [ tx_search setBackgroundColor :[ UIColor clearColor ]];
        
    }
    
    
    
    [iv_topTitle addSubview:tx_search];
    [self setNavCustomerView:iv_topTitle];

    
}
- (void)btnSearchClick{
    
    BMSQ_ShopController* shopCtrl = [[BMSQ_ShopController alloc]init];
    shopCtrl.typeInt = 0;
    shopCtrl.kbShow = YES;
    shopCtrl.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:shopCtrl animated:YES];
}
- (void)btnCityClick{
    self.m_tableViewCity.hidden = !self.m_tableViewCity.hidden;
}
#pragma mark --城市TableView--
-(void)addCityTable{
    self.m_tableViewCity = [[UITableView alloc]initWithFrame:CGRectMake(0, 64, 100, 200)];
    self.m_tableViewCity.dataSource = self;
    self.m_tableViewCity.delegate = self;
    self.m_tableViewCity.backgroundColor = [UIColor clearColor];
    self.m_tableViewCity.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.m_tableViewCity.showsVerticalScrollIndicator = NO;
    self.m_tableViewCity.tag = CITYTABLETAG;
    [self.view addSubview:self.m_tableViewCity];
    self.m_tableViewCity.hidden = YES;
    [self.view insertSubview:self.m_tableViewCity aboveSubview:self.m_tableView];
}



#pragma mark - UITableView Delegate

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (tableView.tag == CITYTABLETAG) {
        return 1;
    }else{
            return self.rowCount;
    }
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (tableView.tag == 100)
    {
         return self.m_dataSourceCity.count;
    }
    else
    {
        
        NSDictionary *dic = [self.m_dataSource objectAtIndex:section];
        
        int moduleValue = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"moduleValue"]]intValue];
        if (moduleValue == 1) {  //滚屏
            return 1;
        }else if (moduleValue == 2){ //分类
            NSArray *subList = [dic objectForKey:@"subList"];
            if (subList.count<8) {
                return 1;
            }else{
                return 2;
            }
        }else if (moduleValue == 3){  //商圈
            return 1;

        }else if (moduleValue == 4){  //品牌
            return 1;

        }else if (moduleValue == 5){  //活动
            return 1;

        }else if (moduleValue == 6){  //推荐
            return self.m_dataSourceShop.count;

        }else if(moduleValue == 7){ //功能
            return 1;
        }else{
            return 0;
        }

        
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView.tag == CITYTABLETAG){
        static NSString* cellIdentify = @"BMSQ_homeCityCell";
        UITableViewCell* cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = APP_NAVCOLOR;
            
        }
        NSDictionary* dicName = [self.m_dataSourceCity objectAtIndex:indexPath.row];
        cell.textLabel.text = [dicName objectForKey:@"name"];
        [cell.textLabel setFont:[UIFont systemFontOfSize:14]];
        cell.textLabel.textColor = [UIColor whiteColor];
        return cell;
    }
    else{
            NSDictionary *dic = [self.m_dataSource objectAtIndex:indexPath.section];
            return [self returnCell:dic tableView:tableView IndexPath:indexPath];
    }
        
   
}

-(UITableViewCell *)returnCell:(NSDictionary *)dic tableView:(UITableView *)tableView IndexPath:(NSIndexPath *)indexPath{
    
    
    int moduleValue = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"moduleValue"]]intValue];
    if (moduleValue == 1) {  //滚屏
        static NSString* cellIdentify = @"BMSQ_HomeCellSetction0";
        BMSQ_adCell* cell = (BMSQ_adCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil) {
            cell = [[BMSQ_adCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.adDelegate = self;
        }
        NSArray *subList = [dic objectForKey:@"subList"];
        [cell setHomeAdCell:subList height:ADCELLH];

        
        return cell;
    }
    else if (moduleValue == 2){ //分类
        static NSString *section1 = @"BMSQ_HomeCellSetction1";
        BMSQ_homeTypeCell *cell = [tableView dequeueReusableCellWithIdentifier:section1];
        if (cell == nil) {
            cell = [[BMSQ_homeTypeCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:section1];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            cell.typeDelegate = self;
        }

        NSArray *array = [dic objectForKey:@"subList"];
        if (array.count<8) {
             [cell setHomeTypeCell:array height:TYPECELLH];
        }else{
            if (indexPath.row==0) {
                [cell setHomeTypeCell:[array subarrayWithRange:NSMakeRange(0, array.count/2)] height:TYPECELLH];

            }else{
                [cell setHomeTypeCell:[array subarrayWithRange:NSMakeRange(array.count/2,array.count-array.count/2 )] height:TYPECELLH];
            }
        }
        
        return cell;
    }
    else if (moduleValue == 3){  //商圈
        static NSString *section2 = @"BMSQ_HomeCellSetction2";
        BMSQ_homeCircleSquareTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:section2];
        if (cell == nil) {
            cell = [[BMSQ_homeCircleSquareTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:section2];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            cell.circleDelegate = self;
        }
        
        NSArray *subList = [dic objectForKey:@"subList"];
        NSDictionary *subDic = [subList objectAtIndex:0];
        float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
        [cell homeCircleSquare:subList height:rate>0?[self retrunH:rate :subList.count>1]:CIRCLECELLH];
//        NSArray *array = [dic objectForKey:@"subList"];
//        [cell homeCircleSquare:array height:CIRCLECELLH];
        return cell;
    }
    else if (moduleValue == 4){  //品牌
        static NSString *section3 = @"BMSQ_HomeCellSetction3";
        BMSQ_homeBrandTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:section3];
        if (cell == nil) {
            cell = [[BMSQ_homeBrandTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:section3];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            cell.brandDelegate = self;
            cell.currenType = 100;
        }
        
        
        
//        NSArray *subList = [dic objectForKey:@"subList"];
//        NSDictionary *subDic = [subList objectAtIndex:0];
//        float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
//        float h = rate>0?[self retrunH:rate]:OTHERCELLH;
//        float height =subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
//        [cell setHomeActivityCell:subList height:height];
        
        
        
        NSArray *subList = [dic objectForKey:@"subList"];
        if (!subList.count%2==0 || subList.count>2) {
            NSDictionary *subDic = [subList objectAtIndex:1];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            float height = subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
            [cell setHomeActivityCell:subList height:height];
            
        }else{
            NSDictionary *subDic = [subList objectAtIndex:0];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            float height = subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
            [cell setHomeActivityCell:subList height:height];
            
        }
        
        return cell;
    }
    else if (moduleValue == 5){  //活动
        static NSString *section3 = @"BMSQ_HomeCellSetction3";
        BMSQ_homeBrandTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:section3];
        if (cell == nil) {
            cell = [[BMSQ_homeBrandTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:section3];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            cell.brandDelegate = self;
        }
        
//        NSArray *subList = [dic objectForKey:@"subList"];
//        NSDictionary *subDic = [subList objectAtIndex:0];
//        float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
//        float h = rate>0?[self retrunH:rate]:OTHERCELLH;
//        float height = subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
//        [cell setHomeActivityCell:subList height:height];
        
        
        NSArray *subList = [dic objectForKey:@"subList"];
        if (!subList.count%2==0 || subList.count>2) {
            NSDictionary *subDic = [subList objectAtIndex:1];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            float height = subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
            [cell setHomeActivityCell:subList height:height];

        }else{
            NSDictionary *subDic = [subList objectAtIndex:0];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            float height = subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
            [cell setHomeActivityCell:subList height:height];
            
        }
        
        return cell;
    }
    else if (moduleValue == 7){  //功能
        static NSString *section3 = @"BMSQ_HomeCellSetction3";
        BMSQ_homeBrandTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:section3];
        if (cell == nil) {
            cell = [[BMSQ_homeBrandTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:section3];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            cell.brandDelegate = self;
        }
        
//        NSArray *subList = [dic objectForKey:@"subList"];
//        NSDictionary *subDic = [subList objectAtIndex:0];
//        float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
//        float h = rate>0?[self retrunH:rate]:OTHERCELLH;
//        float height =subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
//        [cell setHomeActivityCell:subList height:height];
        
        
        NSArray *subList = [dic objectForKey:@"subList"];
        if (!subList.count%2==0 || subList.count>2) {
            NSDictionary *subDic = [subList objectAtIndex:1];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            float height = subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
            [cell setHomeActivityCell:subList height:height];
            
        }else{
            NSDictionary *subDic = [subList objectAtIndex:0];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            float height = subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
            [cell setHomeActivityCell:subList height:height];
            
        }
        
        
        
        return cell;
    }
    else if (moduleValue == 6){  //推荐
        if (indexPath.row ==0) {
            static NSString *cellIdentifier = @"BMSQ_homeShopZeroRowCell";
            BMSQ_homeShopZeroRowCell *cell = (BMSQ_homeShopZeroRowCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
            if (cell == nil) {
                cell = [[BMSQ_homeShopZeroRowCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
                cell.selectionStyle=UITableViewCellSelectionStyleNone ;
                cell.zeroDelegate = self;
            }
            if (self.m_dataSourceShop.count==0) {
                
            }else{
                [cell setCellValue:[self.m_dataSourceShop objectAtIndex:indexPath.row] titleDic:dic];
 
            }
            
            return cell;

        }else{
        
        static NSString *cellIdentifier = @"BMSQ_ShopCellcell";
        BMSQ_RecommendShopCell *cell = (BMSQ_RecommendShopCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            cell = [[BMSQ_RecommendShopCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        }
            if(self.m_dataSourceShop.count==0){
                
            }else{
                [cell setCellValue:[self.m_dataSourceShop objectAtIndex:indexPath.row]];
            }
            
        return cell;
        }
        
        
    }
    
    return 0;
    
   

}




- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (tableView.tag == 100) {  //城市列表
        return 44;
        
    }
    else
    {
        NSDictionary *dic = [self.m_dataSource objectAtIndex:indexPath.section];
         return [self returnH:dic IndexPath:indexPath];
    }
    return 0;
}

-(CGFloat)returnH:(NSDictionary *)dic IndexPath:(NSIndexPath *)indexPath{
    int moduleValue = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"moduleValue"]]intValue];
    if (moduleValue == 1) {  //滚屏   左右滑
        return ADCELLH;
    }else if (moduleValue == 2){ //分类  左右滑
        return TYPECELLH;
    }else if (moduleValue == 3){  //商圈   左右滑
        NSArray *subList = [dic objectForKey:@"subList"];
        NSDictionary *subDic = [subList objectAtIndex:0];
        float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
        return rate>0?[self retrunH:rate : subList.count>1]:CIRCLECELLH;
//        return CIRCLECELLH;
    }else if (moduleValue == 4){  //品牌

        NSArray *subList = [dic objectForKey:@"subList"];
        if (!subList.count%2==0 && subList.count>2) {
            NSDictionary *subDic = [subList objectAtIndex:1];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            return subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
        }else{
            NSDictionary *subDic = [subList objectAtIndex:0];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            return subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
 
        }
        
       
//        NSArray *array = [dic objectForKey:@"subList"];
//        float h = OTHERCELLH;
//        return array.count%2==0?array.count/2*h:(array.count/2+1)*h;
    }else if (moduleValue == 5){  //活动
        
        
//        NSArray *subList = [dic objectForKey:@"subList"];
//        NSDictionary *subDic = [subList objectAtIndex:0];
//        float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
//        float h = rate>0?[self retrunH:rate]:OTHERCELLH;
//        return subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;

        
        NSArray *subList = [dic objectForKey:@"subList"];
        if (!subList.count%2==0 && subList.count>2) {
            NSDictionary *subDic = [subList objectAtIndex:1];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            return subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
        }else{
            NSDictionary *subDic = [subList objectAtIndex:0];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            return subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
            
        }
        
        
//        NSArray *array = [dic objectForKey:@"subList"];
//        float h = OTHERCELLH;//APP_VIEW_HEIGHT/6+10;
//        return array.count%2==0?array.count/2*h:(array.count/2+1)*h;
    }else if (moduleValue == 6){  //推荐
        if (indexPath.row ==0) {
            if (self.m_dataSourceShop.count==0) {
                
            }else{
                return [BMSQ_homeShopZeroRowCell cellHeigh:[self.m_dataSourceShop objectAtIndex:indexPath.row]];
            }
            
        }else{
            if (self.m_dataSourceShop.count==0) {
                
            }else{
                return [BMSQ_RecommendShopCell cellHeigh:[self.m_dataSourceShop objectAtIndex:indexPath.row]];
            }
        }
    }else if (moduleValue == 7){  //功能
        
//        NSArray *subList = [dic objectForKey:@"subList"];
//        NSDictionary *subDic = [subList objectAtIndex:0];
//        float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
//        float h = rate>0?[self retrunH:rate]:OTHERCELLH;
//        return subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
        
        
        NSArray *subList = [dic objectForKey:@"subList"];
        if (!subList.count%2==0 && subList.count>2) {
            NSDictionary *subDic = [subList objectAtIndex:1];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            return subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
        }else{
            NSDictionary *subDic = [subList objectAtIndex:0];
            float rate = [[NSString stringWithFormat:@"%@",[subDic objectForKey:@"imgRate"]] floatValue];
            float h = rate>0?[self retrunH:rate : subList.count>1]:OTHERCELLH;
            return subList.count%2==0?subList.count/2*h:(subList.count/2+1)*h;
            
        }

        
//        NSArray *array = [dic objectForKey:@"subList"];
//        float h = OTHERCELLH;//APP_VIEW_HEIGHT/8;
//        return array.count%2==0?array.count/2*h:(array.count/2+1)*h;
    }
    
    return 0;

}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (tableView.tag == 100)
    {
        NSDictionary* dicName = [self.m_dataSourceCity objectAtIndex:indexPath.row];
        [btnCity setTitle:[dicName objectForKey:@"name"] forState:UIControlStateNormal];
        
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:[dicName objectForKey:@"name"] forKey:SELECITY];
        self.m_tableViewCity.hidden = YES;
        [self.m_dataSource removeAllObjects];
        [self.m_dataSourceShop removeAllObjects];
        self.rowCount = 0;
        self.adDic = nil;
        self.typeDic = nil;
        self.cicleDic = nil;
        self.brandDic = nil;
        self.actidDic = nil;
        self.shopdDic = nil;
        self.functionDic = nil;
        pageNumber = 1;
        [SVProgressHUD showWithStatus:@"" maskType:SVProgressHUDMaskTypeClear];
        [self getHomeInfo];
        
        
    }
    else{
        if (indexPath.section == self.m_dataSource.count) {
//            NSDictionary *dic = [self.m_dataSourceShop objectAtIndex:indexPath.row];
//            BMSQ_ShopDetailController* detailCtrl = [[BMSQ_ShopDetailController alloc]init];
//            detailCtrl.shopCode = [dic objectForKey:@"shopCode"];
//            detailCtrl.shopName = [dic objectForKey:@"shopName"];
//            detailCtrl.shopImage = [dic objectForKey:@"logoUrl"];
//            detailCtrl.hidesBottomBarWhenPushed = YES;
//            [self.navigationController pushViewController:detailCtrl animated:YES];
            
        }else{
            NSDictionary *dic = [self.m_dataSource objectAtIndex:indexPath.section];
            
            int moduleValue = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"moduleValue"]]intValue];
            if (moduleValue == 1) {  //滚屏
                
                
             }else if (moduleValue == 2){ //分类
                 
                 
                 
            }else if (moduleValue == 3){  //商圈
                
                
                
            }else if (moduleValue == 4){  //品牌
                
                
                
            }else if (moduleValue == 5){  //活动
                
                
                
            }else if (moduleValue == 6){  //店铺
                [MobClick event:@"recommend_shop"];// 友盟统计
                NSDictionary * newDic = [NSDictionary dictionaryWithDictionary:self.m_dataSourceShop[indexPath.row]];
                //isCatering 0-未知，1-餐饮，2-教育
                int isCatering = [[NSString stringWithFormat:@"%@",[newDic objectForKey:@"isCatering"]]intValue];
                
                if (isCatering == 2) {
                    BMSQ_educationViewController *vc = [[BMSQ_educationViewController alloc]init];
                    vc.shopCode = [newDic objectForKey:@"shopCode"];
                    vc.hidesBottomBarWhenPushed = YES;
                    [self.navigationController pushViewController:vc animated:YES];
                }else {
                    BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
                    detailCtrl.shopCode = [newDic objectForKey:@"shopCode"];
                    detailCtrl.userCode = [gloabFunction getUserCode];
                    detailCtrl.hidesBottomBarWhenPushed = YES;
                    [self.navigationController pushViewController:detailCtrl animated:YES];
                    
                }
            

                
            }
            
        }
            
            
            
      
        
    }
}

#pragma  mark uisearchbar delegate
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchbar
{
    [tx_search resignFirstResponder];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) searchbar
{
    [tx_search resignFirstResponder];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [tx_search resignFirstResponder];
    self.m_tableViewCity.hidden = YES;
}


#pragma mark ---扫描---
- (void)btnRightClick{
    
    [MobClick event:@"home_scan"];// 友盟统计 
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    reader.showsHelpOnFail = NO;
    reader.scanCrop = CGRectMake(0.1, 0.2, 0.8, 0.8);
    
    ZBarImageScanner *scanner = reader.scanner;
    
    [scanner setSymbology: ZBAR_I25
                   config: ZBAR_CFG_ENABLE
                       to: 0];
    
    UIView * view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    view.backgroundColor = [UIColor clearColor];
    reader.cameraOverlayView = view;
    
    UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(20, 20, APP_VIEW_WIDTH - 40, 40)];
    label.text = @"请将扫描的二维码至于下面的框内！";
    label.textColor = [UIColor whiteColor];
    label.textAlignment = 1;
    label.lineBreakMode = 0;
    label.numberOfLines = 2;
    label.backgroundColor = [UIColor clearColor];
    [view addSubview:label];
    
    UIImageView * image = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"pick_bg.png"]];
    image.frame = CGRectMake(20, 80, APP_VIEW_WIDTH-40, APP_VIEW_WIDTH - 40);
    [view addSubview:image];
    
    
    _line = [[UIImageView alloc] initWithFrame:CGRectMake(30, 10, APP_VIEW_WIDTH - 100, 2)];
    _line.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [image addSubview:_line];
    //定时器，设定时间过1.5秒，
    timer = [NSTimer scheduledTimerWithTimeInterval:.02 target:self selector:@selector(animation1) userInfo:nil repeats:YES];
    
    
    
    [self presentViewController:reader animated:YES completion:nil];
    
}
-(void)animation1{
    if (upOrdown == NO) {
        num ++;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (2*num == 260) {
            upOrdown = YES;
        }
    }
    else {
        num --;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (num == 0) {
            upOrdown = NO;
        }
    }
    
    
}
-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker{
    [timer invalidate];
    _line.frame = CGRectMake(30, 10, 220, 2);
    num = 0;
    upOrdown = NO;
    [picker dismissViewControllerAnimated:YES completion:^{
        [picker removeFromParentViewController];
    }];
}
- (void) imagePickerController: (UIImagePickerController*) reader didFinishPickingMediaWithInfo: (NSDictionary*) info{
    [timer invalidate];
    _line.frame = CGRectMake(30, 10, 220, 2);
    num = 0;
    upOrdown = NO;
    
    id<NSFastEnumeration> results =
    [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        break;

    [reader dismissViewControllerAnimated:YES completion: nil];

    
    NSString *scanResult=symbol.data;
    if(scanResult==nil||scanResult.length==0){
        return ;
    }
    NSString *str=@"shopQrCode?id=";
    NSRange range = [scanResult rangeOfString:str];
    if (range.location>0&&range.length>0) {
        scanResult = [scanResult substringFromIndex:NSMaxRange(range)];
        
        BMSQ_ShopDetailController* detailCtrl = [[BMSQ_ShopDetailController alloc]init];
        detailCtrl.shopCode = scanResult;
        //        detailCtrl.couponDic = cell.couponDic;
        detailCtrl.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:detailCtrl animated:YES];
        
    }else{
        range = [scanResult rangeOfString:@"coupon|"];
        if (range.location>0&&range.length>0) {
            scanResult = [scanResult substringFromIndex:NSMaxRange(range)];
            
            if (![gloabFunction isLogin])
            {
                [self getLogin];
                return;
            }
            
            
            [self initJsonPrcClient:@"2"];
            NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
            [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
            [params setObject:scanResult forKey:@"batchCouponCode"];
            [params setObject:@"2" forKey:@"sharedLvl"];
            NSString* vcode = [gloabFunction getSign:@"grabCoupon" strParams:scanResult];
            [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
            [params setObject:vcode forKey:@"vcode"];
            [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
            
            [self.jsonPrcClient invokeMethod:@"grabCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
                
                NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
                if ([code isEqualToString:@"50000"])
                {
                    [self showAlertView:@"抢券成功"];
                    
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
                
                //[m_tableView reloadData];
            } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                [ProgressManage closeProgress];
                [self showAlertView:@"抢券失败"];
            }];
            
            
        }
        
    }
    
    
}


#pragma mark 滚屏
-(void)clickAD:(int)tag{

    NSArray *subList = [self.adDic objectForKey:@"subList"];
    NSDictionary *dic = [subList objectAtIndex:tag];
    self.isSeleType = NO;
    [self gotoNext:dic isType:NO];
    
}
#pragma mark clickType
-(void)clickType:(int)tag typeDic:(NSDictionary *)dic{
    self.isSeleType = YES;
    [self gotoNext:dic isType:YES];
}
#pragma mark 圈广场
-(void)clickCircle:(int)tag{
    
    NSArray *subList = [self.cicleDic objectForKey:@"subList"];
    NSDictionary *dic = [subList objectAtIndex:tag];
    self.isSeleType = NO;

    [self gotoNext:dic isType:NO];
}
#pragma mark 品牌  活动  功能
-(void)clickImage:(NSDictionary *)dic{
    
    [self gotoNext:dic isType:NO];
    
}


/*
-(void)ciclikBrand:(int)tag{

    NSArray *subList = [self.brandDic objectForKey:@"subList"];
    NSDictionary *dic = [subList objectAtIndex:tag];
    self.isSeleType = NO;

    [self gotoNext:dic overAll:self.self.brandDic];

}
#pragma mark 活动
-(void)ciclikActivity:(int)tag{

    NSArray *subList = [self.actidDic objectForKey:@"subList"];
    NSDictionary *dic = [subList objectAtIndex:tag];
    self.isSeleType = NO;

    [self gotoNext:dic overAll:self.self.actidDic];


}
 */
-(void)gotoNext:(NSDictionary *)dic isType:(BOOL )isType{
    int linkType = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"linkType"]]intValue];
    
    if (linkType == 0) {  //h5界面
        BMSQ_homeWebController *vc = [[BMSQ_homeWebController alloc]init];
        NSString *content = [dic objectForKey:@"content"];
        if ([content hasPrefix:@"http://"]) {
            vc.urlStr = content;
        }else{
            NSString *userCode= [gloabFunction getUserCode];
            if (userCode.length>0) {
                vc.urlStr = [NSString stringWithFormat:@"%@%@&userCode=%@",H5_URL,content,userCode];
            }else{
                vc.urlStr = [NSString stringWithFormat:@"%@%@",H5_URL,content];
            }
        }
        vc.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:vc animated:YES];
    }else if (linkType ==1){ // 商家列表
        
//        int i = [[NSString stringWithFormat:@"%@",[alldic objectForKey:@"moduleValue"]]intValue];
        if (isType) {
            //0-所有类型；1-美食；2-咖啡；3-健身；4-娱乐；5-服装；6-其他
            BMSQ_ShopController* detailCtrl = [[BMSQ_ShopController alloc]init];
            detailCtrl.hidesBottomBarWhenPushed = YES;
            detailCtrl.typeInt = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"content"]]intValue];
            detailCtrl.isSeleType = 1;
            detailCtrl.typeStr = [NSString stringWithFormat:@"%@",[dic objectForKey:@"title"]];
            [self.navigationController pushViewController:detailCtrl animated:YES];
//
        }else{
            
            BMSQ_ShopController* detailCtrl = [[BMSQ_ShopController alloc]init];
            detailCtrl.hidesBottomBarWhenPushed = YES;
            detailCtrl.content = [NSString stringWithFormat:@"%@",[dic objectForKey:@"content"]];
//            detailCtrl.moduleValue = [NSString stringWithFormat:@"%@",[alldic objectForKey:@"moduleValue"]];
            detailCtrl.typeInt = 0;
            detailCtrl.isSeleType = 2;  //1 类型 2 广场
            detailCtrl.typeStr = [NSString stringWithFormat:@"%@",[dic objectForKey:@"title"]];
            [self.navigationController pushViewController:detailCtrl animated:YES];
            
            
        }
    
        
    }else if (linkType ==2){  //抢优惠券列表
        BMSQ_GrabvotesViewController *vc = [[BMSQ_GrabvotesViewController alloc]init];
        vc.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:vc animated:YES];
        [MobClick event:@"home_coupon"];
    }else if (linkType ==3){  //工银特惠
        BMSQ_IcbcShopViewController *vc = [[BMSQ_IcbcShopViewController alloc]init];
        vc.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:vc animated:YES];
        [MobClick event:@"home_icb"];
    }
}
-(void)footRefresh{
    [self getHomeShopList];
}
-(void)headRefresh{
    [self.m_tableView headerEndRefreshing];
    
    [self.m_dataSourceShop removeAllObjects];
    pageNumber = 1;
    [self getHomeShopList];


}

#pragma mark -- 请求 --
//首页推荐商家
- (void)getHomeShopList{
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    NSString *city = [userDefults objectForKey:SELECITY];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    [params setObject:[NSString stringWithFormat:@"%d",pageNumber] forKey:@"page"];
    [params setObject:city forKey:@"city"];

    
    NSString* vcode = [gloabFunction getSign:@"getHomeShopList" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself =self;
    [self.jsonPrcClient invokeMethod:@"getHomeShopList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [wself.m_dataSourceShop addObjectsFromArray:[responseObject objectForKey:@"shopList"]];
        int  nextPage = [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"nextPage"]] intValue];// pageNumber+1;
        if (nextPage != pageNumber) {
            pageNumber = nextPage;
        }else {
            pageNumber = pageNumber+1;
        }

        
        [wself.m_tableView reloadData];
        [wself.m_tableView footerEndRefreshing];
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
    
}
//首页模块排版
- (void)getHomeInfo {
    
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@"" maskType:SVProgressHUDMaskTypeClear];
    NSUserDefaults *userdefaults = [NSUserDefaults standardUserDefaults];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString *city =[userdefaults objectForKey:SELECITY];
    [params setObject:city forKey:@"city"];
    
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"getHomeInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [wself.m_dataSource addObjectsFromArray: responseObject];
        [wself.m_tableView headerEndRefreshing];
        wself.rowCount = 0;
        
        for (NSDictionary *dic in wself.m_dataSource) {
            int moduleValue = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"moduleValue"]]intValue];
            if (moduleValue == 1) {  //滚屏
                wself.adDic = dic;
                wself.rowCount = wself.rowCount+1;
            }else if (moduleValue == 2){ //分类
                wself.typeDic = dic;
                wself.rowCount = wself.rowCount+1;

            }else if (moduleValue == 3){  //商圈
                wself.cicleDic = dic;
                wself.rowCount = wself.rowCount+1;

            }else if (moduleValue == 4){  //品牌
                wself.brandDic = dic;
                wself.rowCount = wself.rowCount+1;

            }else if (moduleValue == 5){  //活动
                wself.actidDic = dic;
                wself.rowCount = wself.rowCount+1;

            }else if (moduleValue == 7){  //功能
                wself.functionDic =dic;
                wself.rowCount = wself.rowCount+1;
            }else if (moduleValue == 6){  //推荐
                wself.shopdDic =dic;
                wself.rowCount = wself.rowCount+1;
                [[NSNotificationCenter defaultCenter]postNotificationName:@"shoplist" object:nil];
            }
        }
        
        [wself.m_tableView reloadData];
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [wself.m_tableView headerEndRefreshing];
        
        [SVProgressHUD dismiss];
        
    }];
    
}
//推荐--
-(void)gotoShopVC{
    BMSQ_ShopController* detailCtrl = [[BMSQ_ShopController alloc]init];
    detailCtrl.typeInt = 0;
    detailCtrl.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:detailCtrl animated:YES];
}
//发送用户所在城市
- (void)recordUserAddress{
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];

    
    NSUserDefaults *userdefaults = [NSUserDefaults standardUserDefaults];
    NSDictionary *dic = [userdefaults objectForKey:APP_USER_AOCNUM_KEY];
    NSString *mobileNbr = [NSString stringWithFormat:@"%@",[dic objectForKey:@"mobileNbr"]];
    
    NSString *city =[userdefaults objectForKey:CITY];
    [params setObject:city forKey:@"city"];
    [params setObject:![mobileNbr isEqualToString:@"(null)"]?mobileNbr:@"" forKey:@"mobilNbr"]; //手机号
    
    [params setObject:[gloabFunction getOpenUDID] forKey:@"deviceNbr"];//手机唯一标识

    
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"getHomeInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [wself.m_tableView headerEndRefreshing];
        
        
    }];
    
}

-(float)retrunH:(float)rate :(BOOL)b {
    //rate = 宽/高
    //b = yes /2
    if (b) {
        float reH = (APP_VIEW_WIDTH/2)/rate;
        return reH;
    }else{
        float reH = (APP_VIEW_WIDTH)/rate;
        return reH;
    }
 
}
@end
