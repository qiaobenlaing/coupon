//
//  SDZX_cityListView.m
//  SDBooking
//
//  Created by djx on 14-3-12.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "RRC_cityListView.h"
#import "AppDelegate.h"
#import "SDZX_citySingle.h"
@implementation RRC_cityListView
@synthesize delegate;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        
        [self setViewUp];
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

- (void)setViewUp
{
    mySearchBar = [[UISearchBar alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 44)];
    [mySearchBar setPlaceholder:@"输入城市名称或者首字母查询"];
    mySearchBar.delegate = self;
    mySearchBar.autocapitalizationType = UITextAutocapitalizationTypeNone;
    [self addSubview:mySearchBar];
    
    float version = [[[ UIDevice currentDevice ] systemVersion ] floatValue ];
    
    if ([ mySearchBar respondsToSelector : @selector (barTintColor)]) {
        
        float  iosversion7_1 = 7.1 ;
        
        if (version >= iosversion7_1)
            
        {
            
            //iOS7.1
            
            [[[[ mySearchBar . subviews objectAtIndex : 0 ] subviews ] objectAtIndex : 0 ] removeFromSuperview ];
            
            [ mySearchBar setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
        else
            
        {
            
            //iOS7.0
            
            [ mySearchBar setBarTintColor :[ UIColor clearColor ]];
            
            [ mySearchBar setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
    }
    
    else
        
    {
        
        //iOS7.0 以下
        
        [[ mySearchBar . subviews objectAtIndex : 0 ] removeFromSuperview ];
        
        [ mySearchBar setBackgroundColor :[ UIColor clearColor ]];
        
    }
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 44, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y - 44)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self addSubview:m_tableView];
    
    SDZX_citySingle* citySingle = [SDZX_citySingle sharedCityInstance];
    m_dataSource = [citySingle getAllCity];
    m_cityDicDataSource = [citySingle getAllCitySortFirstWord];
    m_cityKeys = [[m_cityDicDataSource allKeys] sortedArrayUsingSelector:
                  @selector(compare:)];
    m_searchDataSource = [[NSMutableArray alloc]initWithArray:m_dataSource];
    
}

- (void)reloadData
{
    SDZX_citySingle* citySingle = [SDZX_citySingle sharedCityInstance];
    m_searchDataSource = [citySingle getAllCity];
    m_cityDicDataSource = [citySingle getAllCitySortFirstWord];
    [m_tableView reloadData];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    if (![mySearchBar.text isEqualToString:@""])
    {
        return 1;
    }
    return [m_cityDicDataSource count]+1;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (![mySearchBar.text isEqualToString:@""])
    {
        return [m_searchDataSource count];
    }
    else
    {
        if (section == 0)
        {
            return 1;
        }
        NSString* key = [m_cityKeys objectAtIndex:section-1];
        return [[m_cityDicDataSource objectForKey:key] count];
    }
    
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (![mySearchBar.text isEqualToString:@""])
    {
        return 0;
    }
    else
    {
        if (section == 0)
        {
            return 0;
        }
        return 30;
    }
    
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView* v_head = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 30)];
    [v_head setBackgroundColor:[UIColor grayColor]];
    
    NSString* key = [m_cityKeys objectAtIndex:section-1];
    UILabel* lb = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH - 20, 30)];
    [lb setText:key];
    [lb setFont:[UIFont systemFontOfSize:16]];
    [v_head addSubview:lb];
    
     UIView* v_line = [[UIView alloc]initWithFrame:CGRectMake(0, 29, APP_VIEW_WIDTH, 1)];
    [v_line setBackgroundColor:UICOLOR(201, 200, 205, 1.0)];
    [v_head addSubview:v_line];

    return v_head;
}
// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (![mySearchBar.text isEqualToString:@""])
    {
        static NSString* cellIndetify = @"cellIndetify";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIndetify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIndetify];
        }
        
        for (UIView* v in cell.contentView.subviews)
        {
            [v removeFromSuperview];
        }
        
        SDZX_cityObject* obj = [m_searchDataSource objectAtIndex:indexPath.row];
        UILabel* lb_city = [[UILabel alloc]initWithFrame:CGRectMake(10, 12, 200, 20)];
        [lb_city setBackgroundColor:[UIColor clearColor]];
        [lb_city setFont:[UIFont systemFontOfSize:14]];
        [lb_city setTextColor:UICOLOR(96, 96, 96, 1.0)];
        lb_city.text = obj.cityName;
        
        UIView* v_line = [[UIView alloc]initWithFrame:CGRectMake(0, 43, APP_VIEW_WIDTH, 1)];
        [v_line setBackgroundColor:UICOLOR(201, 200, 205, 1.0)];
        
        
        [cell.contentView addSubview:lb_city];
        [cell.contentView addSubview:v_line];
        
        return cell;
    }
    else
    {
        if (indexPath.section == 0)
        {
            static NSString* cellIndetify = @"cellIndetify0";
            UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIndetify];
            if (cell == nil)
            {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIndetify];
            }
            
            for (UIView* v in cell.contentView.subviews)
            {
                [v removeFromSuperview];
                
                
                
            }
            
            
            NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
            NSString *curerenName = [userDefults objectForKey:SELECITY];
            UIButton* btn_head = [[UIButton alloc]initWithFrame:CGRectMake(10, 0, 310, 44)];
            if (curerenName == nil || curerenName <= 0 || [curerenName rangeOfString:@"null"].length > 0)
            {
                [btn_head setTitle:[NSString stringWithFormat:@"不能定位到你所在的当前城市，请手动选择城市"] forState:UIControlStateNormal];
            }
            else
            {
                [btn_head setTitle:[NSString stringWithFormat:@"你所在的当前城市: %@",curerenName] forState:UIControlStateNormal];
                [btn_head addTarget:self action:@selector(btnHeadClick) forControlEvents:UIControlEventTouchUpInside];
            }
            //
            [btn_head.titleLabel setFont:[UIFont systemFontOfSize:14]];
            [btn_head setTitleColor:UICOLOR(96, 96, 96, 1.0) forState:UIControlStateNormal];
            [btn_head setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
            [cell addSubview:btn_head];
            return cell;
        }
        else
        {
            static NSString* cellIndetify = @"cellIndetify";
            UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIndetify];
            if (cell == nil)
            {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIndetify];
            }
            
            for (UIView* v in cell.contentView.subviews)
            {
                [v removeFromSuperview];
            }
            
            NSString* key = [m_cityKeys objectAtIndex:indexPath.section-1];
            NSArray* array = [m_cityDicDataSource objectForKey:key];
            
            SDZX_cityObject* obj = [array objectAtIndex:indexPath.row];
            UILabel* lb_city = [[UILabel alloc]initWithFrame:CGRectMake(10, 12, 200, 20)];
            [lb_city setBackgroundColor:[UIColor clearColor]];
            [lb_city setFont:[UIFont systemFontOfSize:14]];
            [lb_city setTextColor:UICOLOR(96, 96, 96, 1.0)];
            lb_city.text = obj.cityName;
            
            UIView* v_line = [[UIView alloc]initWithFrame:CGRectMake(0, 43, APP_VIEW_WIDTH, 1)];
            [v_line setBackgroundColor:UICOLOR(201, 200, 205, 1.0)];
            
            
            [cell.contentView addSubview:lb_city];
            [cell.contentView addSubview:v_line];
            
            return cell;
        }
    }


}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (![mySearchBar.text isEqualToString:@""])
    {
        SDZX_cityObject* obj = [m_searchDataSource objectAtIndex:indexPath.row];
        
        NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
        NSString *longitude = [userDefults objectForKey:LONGITUDE];
        NSString *latitude  = [userDefults objectForKey:LATITUDE];
        NSString *curerenName = [userDefults objectForKey:SELECITY];
        
//        appDelegate.currentCityCode = [[NSString alloc]initWithFormat:@"%@",obj.cityId];
//        appDelegate.currentCityName = [[NSString alloc]initWithFormat:@"%@",obj.cityName];
        if (delegate != nil && [delegate respondsToSelector:@selector(selectCity:)])
        {
            [delegate selectCity:obj];
        }
    }
    else
    {
        NSString* key = [m_cityKeys objectAtIndex:indexPath.section - 1];
        NSArray* array = [m_cityDicDataSource objectForKey:key];
        SDZX_cityObject* obj = [array objectAtIndex:indexPath.row];
//        appDelegate.currentCityCode = [[NSString alloc]initWithFormat:@"%@",obj.cityId];
//        appDelegate.currentCityName = [[NSString alloc]initWithFormat:@"%@",obj.cityName];
        if (delegate != nil && indexPath.section != 0 && [delegate respondsToSelector:@selector(selectCity:)])
        {
            [delegate selectCity:obj];
        }
    }
    
}

- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    
    return m_cityKeys;
    
}

- (void)btnHeadClick
{
    SDZX_citySingle* citySingle = [SDZX_citySingle sharedCityInstance];

    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    NSString *curerenName = [userDefults objectForKey:SELECITY];
    
    SDZX_cityObject* obj = [citySingle getCityByName:curerenName];
//    appDelegate.currentCityCode = [[NSString alloc]initWithFormat:@"%@",obj.cityId];
    if (delegate != nil && [delegate respondsToSelector:@selector(selectCity:)])
    {
        [delegate selectCity:obj];
    }
}

- (void)scrollViewWillBeginDecelerating:(UIScrollView *)scrollView
{
    [mySearchBar resignFirstResponder];
}

#pragma  mark uisearchbar delegate
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    NSString* text = searchBar.text;
    [m_searchDataSource removeAllObjects];
    for (int i = 0; i < m_dataSource.count; i++)
    {
        SDZX_cityObject* obj = [m_dataSource objectAtIndex:i];
        if ([obj.cityName rangeOfString:text].length > 0)
        {
            [m_searchDataSource addObject:obj];
        }
        
    }
    
    [searchBar resignFirstResponder];
    [m_tableView reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) searchBar
{
    [m_searchDataSource removeAllObjects];
    for (int i = 0; i < m_dataSource.count; i++)
    {
        SDZX_cityObject* obj = [m_dataSource objectAtIndex:i];
       
        [m_searchDataSource addObject:obj];
        
    }
    
    [m_tableView reloadData];
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    if ([searchText isEqualToString:@""])
    {
        [m_searchDataSource removeAllObjects];
        for (int i = 0; i < m_dataSource.count; i++)
        {
            SDZX_cityObject* obj = [m_dataSource objectAtIndex:i];
            
            [m_searchDataSource addObject:obj];
            
        }
        [m_tableView reloadData];
    }

}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [mySearchBar resignFirstResponder];
}

@end
