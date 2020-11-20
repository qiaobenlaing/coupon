//
//  BMSQ_HomeShopTypeView.m
//  BMSQC
//
//  Created by gh on 15/11/22.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_HomeShopTypeView.h"
#import "UIButtonEx.h"
#import "UIImageView+WebCache.h"
#import "UIColor+Tools.h"

@interface BMSQ_HomeShopTypeView() {
    
    float xPosition;
    float btnWidth;
    float btnHeight;
    float xOffset;
    
}

@end



@implementation BMSQ_HomeShopTypeView

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        
        [self setViewUp];
        
        
    }
    return self;
    
}


- (void)setViewUp {
    xPosition = 25;
    btnWidth = 31;
    btnHeight = 31;
    xOffset = (APP_VIEW_WIDTH - 2*xPosition - 5*btnWidth)/4;
    NSArray* array = [NSArray arrayWithObjects:@"美食",@"丽人",@"健身",@"娱乐",@"其他", nil];
    
    self.backgroundColor = UICOLOR(255, 255, 255, 1);
    if (_m_dataSource.count) {
        xOffset = (APP_VIEW_WIDTH - 2*xPosition - _m_dataSource.count*btnWidth)/(_m_dataSource.count-1);
    }
    
    
    
    for (int i = 0; i < array.count; i++)
    {
        
        
        //0-所有类型；1-美食；2-咖啡；3-健身；4-娱乐；5-服装；6-其他
        UIButtonEx* btn = [[UIButtonEx alloc]initWithFrame:CGRectMake(xPosition + i * (btnWidth + xOffset), 10, btnWidth, btnHeight)];
        btn.tag = 4000+(i+1);
        [btn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
        //        [btn setBackgroundImage:[UIImage imageNamed:[array objectAtIndex:i]] forState:UIControlStateNormal];
        [self addSubview:btn];
        
        
        UIImageView* imageView = [[UIImageView alloc]initWithFrame:CGRectMake(xPosition + i * (btnWidth + xOffset), 10, btnWidth, btnHeight)];
        imageView.tag = 5000+(i+1);
        [imageView setImage:[UIImage imageNamed:[array objectAtIndex:i]]];
        [self addSubview:imageView];
        
        
        
        UILabel* lbTmp = [[UILabel alloc]initWithFrame:CGRectMake(xPosition + i * (btnWidth + xOffset), btn.frame.origin.y + btnHeight + 5, btnWidth, 20)];
        lbTmp.tag = 6000+(i+1);
        [lbTmp setBackgroundColor:[UIColor clearColor]];
        [lbTmp setText:[array objectAtIndex:i]];
        [lbTmp setTextAlignment:NSTextAlignmentCenter];
        [lbTmp setFont:[UIFont systemFontOfSize:14]];
        [lbTmp setTextColor:UICOLOR(84, 84, 84, 1.0)];
        [self addSubview:lbTmp];
        
        btn.hidden = YES;
        imageView.hidden = YES;
        lbTmp.hidden = YES;
        
    }
    
    
}

- (void)setViewValue:(NSDictionary *)dic {
    
    NSArray *shopTypearray = [dic objectForKey:@"subList"];
    
    
    xOffset = (APP_VIEW_WIDTH - 2*xPosition - shopTypearray.count*btnWidth)/(shopTypearray.count-1);
//    xOffset = (APP_VIEW_WIDTH - 2*xPosition - 3*btnWidth)/2;//test
    
    for (int i =0; i<shopTypearray.count; i++) {
        
        
        NSDictionary *shopTypeDic = shopTypearray[i];
        
        self.backgroundColor = [UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[shopTypeDic objectForKey:@"bgColor"]]];

        
        UIButtonEx *btn;
        UIImageView *imageView;
        UILabel *lb_type;
        
        if (![shopTypeDic objectForKey:@"subModulePosition"]) {
            btn = [self viewWithTag:4000+(i+1)];
            imageView = [self viewWithTag:5000+(i+1)];
            lb_type = [self viewWithTag:6000+(i+1)];
            
        }else  {
            NSString *sign = [shopTypeDic objectForKey:@"subModulePosition"];
            
            btn = [self viewWithTag:4000+(sign.intValue)];
            imageView = [self viewWithTag:5000+(sign.intValue)];
            lb_type = [self viewWithTag:6000+(sign.intValue)];
            
            
        }
        
        
        btn.frame = CGRectMake(xPosition + i * (btnWidth + xOffset), 10, btnWidth, btnHeight);
        imageView.frame = CGRectMake(xPosition + i * (btnWidth + xOffset), 10, btnWidth, btnHeight);
        lb_type.frame = CGRectMake(xPosition + i * (btnWidth + xOffset), btn.frame.origin.y + btnHeight + 5, btnWidth, 20);
        
        btn.hidden = NO;
        imageView.hidden = NO;
        lb_type.hidden = NO;
        
        
        btn.object = shopTypeDic;
        [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL, [shopTypeDic objectForKey:@"imgUrl"] ] ] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
        lb_type.text = [shopTypeDic objectForKey:@"title"];
        lb_type.textColor = [UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[shopTypeDic objectForKey:@"titleColor"]]];

    }
}



- (void)btnAction:(UIButtonEx *)sender {
    if (self.ShopTypeDelegate != nil) {
        [self.ShopTypeDelegate btnShopTypeClick:sender.object];
        
    }
    
}




@end
