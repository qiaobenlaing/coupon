//
//  BMSQ_HomeBusinessCircleView.m
//  BMSQC
//
//  Created by gh on 15/11/22.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_HomeBusinessCircleView.h"
#import "UIColor+Tools.h"
#import "UIImageView+WebCache.h"

@implementation BMSQ_HomeBusinessCircleView

- (id)initWithFrame:(CGRect)frame {
    
    self = [super initWithFrame:frame];
    if (self) {
        
        [self setViewUp];
        
        
    }
    return self;
}


- (void)setViewUp {
    
}

- (void)setBusinessCircleViewUp:(NSDictionary *)dic {
    NSLog(@"%@",dic);
    NSArray *array = [dic objectForKey:@"subList"];
    

    for (int i=0; i<array.count; i++) {
        NSDictionary *subDic = array[i];
        
        UIButtonEx *btn = [[UIButtonEx alloc] init];
        
        
        if (array.count == 1) {
            btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
            
        }else if (array.count == 2) {
            if (i == 0) {
                btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else {
                 btn.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }
        }else if (array.count == 4) {
            if (i == 0) {
                btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 1) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 2) {
                btn.frame =CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 3) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }
            
        }else if (array.count == 6) {
            if (i == 0) {
                btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 1) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 2) {
                btn.frame =CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 3) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 4) {
                btn.frame = CGRectMake(0, HOME_MODULE_HEIGHT*2, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }else if (i == 5) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*2, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
            }
            
        }
        
        
        [self addSubview:btn];
        
        btn.object = subDic;
        btn.tag = 30000+i+1;
        btn.backgroundColor = [UIColor colorWithHexString:@"#FFFFFF"];
        [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
        
        UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(5, 5, APP_VIEW_WIDTH/2-10, HOME_MODULE_HEIGHT-10)];
       
        if (array.count == 1) {
            iv.frame = CGRectMake(5, 5, APP_VIEW_WIDTH-10, HOME_MODULE_HEIGHT*2 - 10);
            
        }
        [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[subDic objectForKey:@"imgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
        iv.contentMode = UIViewContentModeScaleToFill;
        [btn addSubview:iv];
        
        
        
    }

}

- (void)btnAct:(UIButtonEx *)sender {
    if (self.delegate != nil) {
        [self.delegate clickBusinessView:sender.object];
    }
    
    
}


@end
