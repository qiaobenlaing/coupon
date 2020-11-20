//
//  TeactherCollectionCell.h
//  BMSQC
//
//  Created by liuqin on 16/3/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TeactherCollectionCell : UICollectionViewCell
@property (nonatomic, strong)NSString *teacherCode;


-(void)setTeacher:(NSDictionary *)dic row:(int)row;

@end
