//
//  ClassSyllabusCell.h
//  BMSQS
//
//  Created by gh on 16/3/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ClassSyllabusCell : UITableViewCell

@property (nonatomic, strong)UILabel *timeLabel;

- (void)setCellValue:(NSDictionary *)timeDic;


@end
