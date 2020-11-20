//
//  BMSQ_mineOrderViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/1.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_mineOrderViewController.h"
#import "BMSQ_AwayOrderViewController.h"
//#import "MJRefresh.h"

@interface BMSQ_mineOrderViewController ()<UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate,UISearchBarDelegate>
{
//    BOOL  refreshing;
//    int   pageNumber;
//    NSNumber * keyWord;//关键字
//    a
//    int isCatering;//门店订单
//    int isOuttake;// 外卖订单
//    
//    int eatIn;//门店订单数量
//    int outtakeValue;//外卖订单数量
    
}


@property (nonatomic, strong)UITableView * baseView;
@property (nonatomic, strong) NSMutableArray * dataSource;

@property (nonatomic, strong)NSString *isFoodTypeTakeOut;//1 餐饮外卖 0 非餐饮
@property (nonatomic, strong)UISearchBar *tx_search;

@property (nonatomic, strong)NSString *searchStr;

@end

@implementation BMSQ_mineOrderViewController



- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"我的订单"];
    

    [self addSearch];
    
    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
    self.isFoodTypeTakeOut = [uD objectForKey:SHOPTYPE_FOOD_TAKEOUT];
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+60, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.baseView.backgroundColor = [UIColor clearColor];
    self.baseView.dataSource = self;
    self.baseView.delegate = self;
    [self.view addSubview:self.baseView];
    [self.baseView setScrollEnabled:NO];
    [self setExtraCellLineHidden:self.baseView];

}
-(void)addSearch{
    UIView *searchView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 50)];
    searchView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:searchView];

    
    self.tx_search = [[UISearchBar alloc]initWithFrame:CGRectMake(5, 10, APP_VIEW_WIDTH - 20, 30)];
    self.tx_search.delegate = self;
    self.tx_search.returnKeyType = UIReturnKeySearch;
    [self.tx_search setPlaceholder:@"请输入订单号/手机号/餐号"];
    [searchView addSubview:self.tx_search];
    UITextField *searchField = [self.tx_search valueForKey:@"_searchField"];
    searchField.backgroundColor = self.view.backgroundColor;
    searchField.font = [UIFont systemFontOfSize:13.f];

    
    
    
//    UIButton *searchBtn = [[UIButton alloc]initWithFrame:CGRectMake(self.tx_search.frame.size.width, 0, 40, 50)];
//    [searchBtn setTitle:@"查询" forState:UIControlStateNormal];
//    [searchBtn setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
//    searchBtn.titleLabel.font =[UIFont systemFontOfSize:14];
//    [searchBtn addTarget:self action:@selector(clickSearch) forControlEvents:UIControlEventTouchUpInside];
//    [searchView addSubview:searchBtn];
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
-(void)setExtraCellLineHidden: (UITableView *)tableView
{
    UIView *view = [UIView new];
    view.backgroundColor = [UIColor clearColor];
    [tableView setTableFooterView:view];
}


#pragma mark ----- UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [self.isFoodTypeTakeOut isEqualToString:@"1"]?2:1;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString * cell_id = @"mineOrderViewController";
    UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        cell.selectionStyle = UITableViewCellSelectionStyleDefault;
        cell.selectionStyle=UITableViewCellSelectionStyleNone;
        cell.textLabel.font = [UIFont systemFontOfSize:14.f];
        cell.textLabel.textColor = APP_TEXTCOLOR;
        
    }

    if (indexPath.row == 0) {
        cell.textLabel.text = @"门面订单";
    }else{
        cell.textLabel.text = @"外卖订单";

    }
    
        
   
    return cell;
}
#pragma mark ----- UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 0) {
        BMSQ_AwayOrderViewController * awayVC = [[BMSQ_AwayOrderViewController alloc] init];
        awayVC.orderTypeValue = [NSNumber numberWithInt:20];
        awayVC.searchStr = self.searchStr;
        [self.navigationController pushViewController:awayVC animated:YES];
    }else if (indexPath.row == 1){
        BMSQ_AwayOrderViewController * awayVC = [[BMSQ_AwayOrderViewController alloc] init];
        awayVC.orderTypeValue = [NSNumber numberWithInt:21];
        awayVC.searchStr = self.searchStr;
        [self.navigationController pushViewController:awayVC animated:YES];
    }
}

-(void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    self.searchStr =  searchBar.text;
    [searchBar resignFirstResponder];
}
-(void)searchBarTextDidEndEditing:(UISearchBar *)searchBar{
    self.searchStr =  searchBar.text;
    [searchBar resignFirstResponder];


}

@end
