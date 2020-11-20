//
//  BMSQ_homeBrandTableViewCell.h
//  BMSQC
//
//  Created by liuqin on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>



@protocol HomeBrandViewDelegate <NSObject>

-(void)HomeBrandViewDelegate:(NSDictionary *)dic;

@end


@interface HomeBrandView : UIView

//@property (nonatomic, strong)UILabel *titleLabel;
//@property (nonatomic, strong)UILabel *contentLabel;
//@property (nonatomic, strong)UILabel *shopName;
//-(void)setHomeBrand:(NSString *)title icon:(NSString *)iconStr connent:(NSString *)conStr isActivity:(BOOL)b;
//-(void)setAcityBrandFrame:(NSDictionary *)dic;
//-(void)onlyFrame:(NSString *)iconStr;


@property (nonatomic, strong)UIImageView *bgImage;  //背景图
@property (nonatomic, strong)NSDictionary *myDic;

@property (nonatomic, weak)id<HomeBrandViewDelegate>delegate;

-(void)setIconImageStr:(NSString *)iconStr;

@end




@protocol BMSQ_homeBrandTableViewCellDelegate <NSObject>

//-(void)ciclikBrand:(int)tag;
//-(void)ciclikActivity:(int)tag;

-(void)clickImage:(NSDictionary *)dic;

@end

@interface BMSQ_homeBrandTableViewCell : UITableViewCell<HomeBrandViewDelegate>

@property (nonatomic, assign)float secH;
@property (nonatomic, assign)BOOL isActivity;


@property (nonatomic, assign)int currenType;



@property (nonatomic, weak)id<BMSQ_homeBrandTableViewCellDelegate>brandDelegate;

//-(void)setHomeBrandCell:(NSArray *)reponsend height:(float)height;
-(void)setHomeActivityCell:(NSArray *)reponsend height:(float)height;


@end
