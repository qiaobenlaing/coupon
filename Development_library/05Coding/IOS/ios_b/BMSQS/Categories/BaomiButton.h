//
//  BaomiButton.h
//  BMSQS
//
//  Created by liuqin on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BaomiButton : UIButton

/*
 * 定义block
 */
@property (nonatomic,copy) void (^CMActionBlock) (void);

@end
