//
//  UIViewController_SDZX.h
//  SDBooking
//
//  Created by icarddjx on 14-1-21.
//  Copyright (c) 2014年 djx. All rights reserved.
//

//时代在线viewcontroller扩展类


#import "RRC_cityListView.h"

#import "AFJSONRPCClient.h"


@interface UIViewControllerEx : UIViewController<cityListDelegate,UITableViewDelegate>

@property(nonatomic, strong)UIView  *navigationView; //导航条
@property(nonatomic, strong)UILabel *navTitleView; //导航条标题
@property(nonatomic,strong)AFJSONRPCClient* jsonPrcClient;

@property(nonatomic,strong)UIImageView* noDateImage;//数据为空时 默认图片


//设置导航条
- (void)setNavigationBar;

//0通用api，1商家端api,2顾客端api
- (void)initJsonPrcClient:(NSString *)requestType;
//导航条显示/隐藏 isHidden为YES，隐藏，NO显示
- (void)setNavHidden:(BOOL)isHidden;
//导航条返回按钮
- (void)setNavBackItem;
//导航条返回按钮显示/隐藏 isHidden为YES，隐藏，NO显示
- (void)setNavBackItemHidden:(BOOL)isHidden;
//设置左侧按钮
- (void)setNavLeftBarItem:(UIView*)leftBarItem;
//设置右侧按钮
- (void)setNavRightBarItem:(UIView*)rightBarItem;
//设置标题
- (void)setNavTitle:(NSString*)strTitle;
- (void)setRight:(UIButton *)button;


//自定义view
- (void)setNavCustomerView:(id)customerView;
//设置导航栏背景色
- (void)setNavBackGroundColor:(UIColor*)color;

//设置左侧城市按钮
- (void)setNavLeftMapItem;
- (void)setMapCity;

- (void)showAlertView:(NSString*)msg;

- (void)btnLeftMapClick;

- (void)btnShare:(NSString*)shareText shareImg:(UIImage*)shareImage;


//没有数据时，显示错误信息
- (void)showNoDataView;
- (void)hiddenNoDataView;
- (void)setNoDataImg:(NSString*)imgName;

@end
