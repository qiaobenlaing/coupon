//
//  NyfInsertsLabel.h
//  NyfPlugin
//
//  Created by apple on 15/10/15.
//  Copyright © 2015年 apple. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NyfInsertsLabel : UILabel

@property(nonatomic) UIEdgeInsets insets;

-(id)initWithFrame:(CGRect)frame andInsets:(UIEdgeInsets)insets;

-(id)initWithInsets:(UIEdgeInsets)insets;


@end
