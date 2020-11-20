//
//  BMSQ_HomeModuleCell.h
//  BMSQC
//
//  Created by gh on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol HomeModeCellDeleagte <NSObject>

- (void)ClickCSView:(NSDictionary *)dic;
- (void)gotoLinkType:(id)object;


@end



@interface BMSQ_HomeModuleCell : UITableViewCell

@property (nonatomic)id<HomeModeCellDeleagte> delegate;

+ (CGFloat)homeModuleCellHeight:(id)dataSource;

- (void)setHomeModuleCellValue:(NSArray *)dataSource;


@end
