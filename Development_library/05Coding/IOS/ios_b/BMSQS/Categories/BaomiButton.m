//
//  BaomiButton.m
//  BMSQS
//
//  Created by liuqin on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BaomiButton.h"

@implementation BaomiButton

- (id)initWithFrame:(CGRect)frame{
    if (self=[super initWithFrame:frame]) {
        
        [self addTarget:self action:@selector(cmClick:) forControlEvents:UIControlEventTouchUpInside];
    }
    return self;
}

- (void)setFrame:(CGRect)frame{
    [super setFrame:frame];
    [self addTarget:self action:@selector(cmClick:) forControlEvents:UIControlEventTouchUpInside];
}

- (void)cmClick:(id)sender{
    if (self.CMActionBlock) {
        self.CMActionBlock();
    }
}


@end
