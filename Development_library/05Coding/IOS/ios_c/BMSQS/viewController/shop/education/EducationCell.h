//
//  EducationCell.h
//  BMSQC
//
//  Created by liuqin on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface EducationCell : NSObject

@property (nonatomic,strong)NSDictionary *shopInfo;
@property (nonatomic,strong)NSArray *shopTeacher;
@property (nonatomic,strong)NSDictionary *shopRecruitInfo;
@property (nonatomic,strong)NSDictionary *studentStar;
@property (nonatomic,strong)NSArray *aroundShop;
@property (nonatomic, strong)NSArray *recentVisitor;
-(UITableViewCell *)setMainCell:(UITableView *)tableView indexPath:(NSIndexPath *)indexPath;



+(UITableViewCell *)setClassInfo:(UITableView *)tableView class:(NSDictionary *)classDic left:(NSArray *)leftArray indexPath:(NSIndexPath *)indexPath  classTime:(NSString *)classTime classHeigh:(float)classHeigh;

@end
