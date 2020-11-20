//
//  BMSQ_HomeBrandView.m
//  BMSQC
//
//  Created by gh on 15/11/22.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_HomeBrandView.h"
#import "UIColor+Tools.h"
#import "UIImageView+WebCache.h"

@implementation BMSQ_HomeBrandView


- (id)initWithFrame:(CGRect)frame {
    
    self = [super initWithFrame:frame];
    if (self) {
        
        [self setViewUp];
        
        
    }
    return self;
}


- (void)setViewUp {
    
}

- (void)setBrandView:(NSDictionary *)dictionary {
    CGFloat ivY = 10;
    
    CGColorSpaceRef colorSpaceRef = CGColorSpaceCreateDeviceRGB();
    
    CGColorRef color = CGColorCreate(colorSpaceRef, (CGFloat[]){0.1,0,0,0.1});
    
    
    
    NSArray *array = [dictionary objectForKey:@"subList"];
    for (int i=0; i<array.count; i++) {
        NSDictionary *subDic = array[i];
        
        UIButtonEx *btn = [[UIButtonEx alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT)];
        btn.backgroundColor = [UIColor colorWithHexString:@"FFFFFF"];
        btn.tag = 40000+i+1;
        [btn.layer setBorderWidth:0.5];//设置边界的宽度
        [btn.layer setBorderColor:color];//设置边界的颜色
        btn.object = subDic;
        [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:btn];
        
        if (array.count == 1) {
            btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT);
            
        }else if (array.count == 2) {
            if (i==0) {
                btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 1) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }

        }else if (array.count == 4) {
            if (i==0) {
                btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 1) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 2) {
                btn.frame = CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 3) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }
            
        }
        
        UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-HOME_MODULE_HEIGHT+ivY, ivY, HOME_MODULE_HEIGHT-2*ivY, HOME_MODULE_HEIGHT-2*ivY)];
        [btn addSubview:iv];
        [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[subDic objectForKey:@"imgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
        
        UILabel *lb = [[UILabel alloc] initWithFrame:CGRectMake(5, 0, APP_VIEW_WIDTH/2-HOME_MODULE_HEIGHT, HOME_MODULE_HEIGHT)];
        lb.text = [NSString stringWithFormat:@"%@",[subDic objectForKey:@"title"]];
        [btn addSubview:lb];
        
    }
    
    
}

- (void)btnAct:(UIButtonEx *)sender {
    
    if (self.delegate != nil) {
        
        [self.delegate touchBrandView:sender.object];
        
    }
    
}


@end
