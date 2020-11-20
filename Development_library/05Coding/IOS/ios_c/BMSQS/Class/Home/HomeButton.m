//
//  HomeButton.m
//  BMSQC
//
//  Created by gh on 15/11/20.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "HomeButton.h"

#import "UIImageView+WebCache.h"

#define TITLE_FONT 13
#define SUB_TITLE_FONT 11


@implementation HomeButton

+ (UIButtonEx *)createHomeActivityBtn:(CGRect)frame
                               object:(NSDictionary *)dictionary
                                  tag:(NSInteger)tag{
    

    
    UIButtonEx *btn = [[UIButtonEx alloc] initWithFrame:frame];
    if ([dictionary objectForKey:@"bgColor"]) {
        [btn setBackgroundColor:[UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"bgColor"]]]];

    }
    btn.tag = tag;
    btn.object = dictionary;
    btn.clipsToBounds = YES;
    
    CGColorSpaceRef colorSpaceRef = CGColorSpaceCreateDeviceRGB();
    
    CGColorRef color = CGColorCreate(colorSpaceRef, (CGFloat[]){0.1,0,0,0.1});
    
    [btn.layer setBorderWidth:0.5];//设置边界的宽度
    
    [btn.layer setBorderColor:color];//设置边界的颜色
    
    
    CGFloat ivY = 10;
    CGFloat ivHeight = APP_VIEW_WIDTH/5-2*ivY;
    
    UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(ivY, ivY, ivHeight, ivHeight)];
    [btn addSubview:iv];
    iv.backgroundColor = [UIColor whiteColor];
    [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dictionary objectForKey:@"imgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    [btn addSubview:iv];
    iv.clipsToBounds = YES;
    
    UILabel *lb_title = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/5, 0, APP_VIEW_WIDTH/2 - HOME_MODULE_HEIGHT, HOME_MODULE_HEIGHT/2)];
    lb_title.textColor = [UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"titleColor"]]];
    lb_title.text = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"title"]];
    lb_title.font = [UIFont systemFontOfSize:TITLE_FONT];
    [btn addSubview:lb_title];
    
    UILabel *lb_subTitle = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/5, APP_VIEW_WIDTH/5/2, APP_VIEW_WIDTH/2 - APP_VIEW_WIDTH/5, APP_VIEW_WIDTH/5/2)];
    lb_subTitle.textColor = [UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"subTitleColor"]]];
    lb_subTitle.text = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"subTitle"]];
    lb_subTitle.font = [UIFont systemFontOfSize:SUB_TITLE_FONT];
    [btn addSubview:lb_subTitle];
    
    //换行
    lb_title.numberOfLines = 0;
    lb_title.lineBreakMode = NSLineBreakByCharWrapping;
    lb_subTitle.numberOfLines = 0;
    lb_subTitle.lineBreakMode = NSLineBreakByCharWrapping;

    
    if ([[dictionary objectForKey:@"imgHeight"] floatValue]>0) {
        iv.frame = CGRectMake(iv.frame.origin.x, iv.frame.origin.y, [[dictionary objectForKey:@"imgWidth"] floatValue], [[dictionary objectForKey:@"imgHeight"] floatValue]);

        lb_title.frame = CGRectMake(iv.frame.origin.x +iv.frame.size.width, 0, btn.frame.size.width-(iv.frame.origin.x +iv.frame.size.width)-10, HOME_MODULE_HEIGHT/2);
        lb_subTitle.frame =CGRectMake(iv.frame.origin.x +iv.frame.size.width, HOME_MODULE_HEIGHT/2, APP_VIEW_WIDTH/2-(iv.frame.origin.x +iv.frame.size.width)-10, HOME_MODULE_HEIGHT/2);
 
    }

    int imgPosition = [[dictionary objectForKey:@"imgPosition"] intValue];
    if (imgPosition == 2) {//右边
        iv.frame = CGRectMake(APP_VIEW_WIDTH/2-iv.frame.size.width-10, ivY, iv.frame.size.width, iv.frame.size.height);
        lb_title.frame = CGRectMake(10,  0, APP_VIEW_WIDTH/2 - (iv.frame.size.width+ivY)-10, HOME_MODULE_HEIGHT/2);
        lb_subTitle.frame = CGRectMake(10, HOME_MODULE_HEIGHT/2, APP_VIEW_WIDTH/2 - (iv.frame.size.width+ivY)-10, HOME_MODULE_HEIGHT/2);

    }
    return btn;
}


//垂直的活动按钮
+ (UIButtonEx *)createUprightActivityBtn:(CGRect)frame
                                  object:(NSDictionary *)dictionary
                                     tag:(NSInteger)tag {
    UIButtonEx *btn = [[UIButtonEx alloc] initWithFrame:frame];
    
    if ([dictionary objectForKey:@"bgColor"]) {
        [btn setBackgroundColor:[UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"bgColor"]]]];
        
    }
    btn.tag = tag;
    btn.object = dictionary;
    btn.clipsToBounds = YES;
    
    CGColorSpaceRef colorSpaceRef = CGColorSpaceCreateDeviceRGB();
    CGColorRef color = CGColorCreate(colorSpaceRef, (CGFloat[]){0.1,0,0,0.1});
    [btn.layer setBorderWidth:0.5];//设置边界的宽度
    [btn.layer setBorderColor:color];//设置边界的颜色
    
    CGFloat ivY = 5;
    CGFloat frameHeight = frame.size.height;
    
    UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(ivY, ivY, frame.size.width-2*ivY, frameHeight/2 - 2*ivY)];
    [btn addSubview:iv];
    iv.backgroundColor = [UIColor whiteColor];
    [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dictionary objectForKey:@"imgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    [btn addSubview:iv];
    iv.clipsToBounds = YES;
    
    UILabel *lb_title = [[UILabel alloc] initWithFrame:CGRectMake(10, frameHeight/2, APP_VIEW_WIDTH/2, frame.size.height/2)];
    lb_title.textColor = [UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"titleColor"]]];
    lb_title.text = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"title"]];
    lb_title.font = [UIFont systemFontOfSize:TITLE_FONT];
    [btn addSubview:lb_title];
    
    
    UILabel *lb_subTitle = [[UILabel alloc] initWithFrame:CGRectMake(10, frameHeight/2+frameHeight/4, APP_VIEW_WIDTH/2-20, frame.size.height/2)];
    lb_subTitle.textColor = [UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"subTitleColor"]]];
    lb_subTitle.text = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"subTitle"]];
    lb_subTitle.font = [UIFont systemFontOfSize:SUB_TITLE_FONT];
    [btn addSubview:lb_subTitle];
    
    lb_title.numberOfLines = 0;
    lb_title.lineBreakMode = NSLineBreakByCharWrapping;
    lb_subTitle.numberOfLines = 0;
    lb_subTitle.lineBreakMode = NSLineBreakByCharWrapping;
    

    int imgPosition = [[dictionary objectForKey:@"imgPosition"] intValue];
    if ([[dictionary objectForKey:@"imgHeight"] floatValue]>0) {
        iv.frame = CGRectMake(iv.frame.origin.x, iv.frame.origin.y, [[dictionary objectForKey:@"imgWidth"] floatValue], [[dictionary objectForKey:@"imgHeight"] floatValue]);

        
    }
    
    switch (imgPosition) {
        case 0:{
            iv.frame = CGRectMake(ivY, ivY, iv.frame.size.width, iv.frame.size.height);
            lb_title.frame = CGRectMake(ivY+iv.frame.size.width,  ivY, lb_title.frame.size.width - ivY-iv.frame.size.width, frame.size.height/2);
            lb_subTitle.frame = CGRectMake(ivY+iv.frame.size.width, frame.size.height/2, lb_subTitle.frame.size.width - ivY-iv.frame.size.width, frame.size.height/2);
            
        }
            break;
            
        case 1:{

            lb_title.frame = CGRectMake(lb_title.frame.origin.x, iv.frame.origin.y+iv.frame.size.height, lb_title.frame.size.width, lb_title.frame.size.height);
            lb_subTitle.frame = CGRectMake(lb_subTitle.frame.origin.x, lb_title.frame.size.height+lb_title.frame.origin.y, lb_subTitle.frame.size.width, lb_subTitle.frame.size.height);
            
        }
            break;
            
        case 2: {
            iv.frame = CGRectMake(frame.size.width-iv.frame.size.width, ivY, iv.frame.size.width, iv.frame.size.height);
            lb_title.frame = CGRectMake(ivY, ivY, lb_title.frame.size.width-iv.frame.size.width, frame.size.height/2);
            lb_subTitle.frame = CGRectMake(ivY, frame.size.height/2, lb_title.frame.size.width, frame.size.height/2);
            
            
        }
            break;
            
        case 3:{
            iv.frame = CGRectMake(ivY, frame.size.height/2, iv.frame.size.width, iv.frame.size.height);
            lb_title.frame = CGRectMake(10,  0, lb_title.frame.size.width, lb_title.frame.size.height);
            lb_subTitle.frame = CGRectMake(10, frame.size.height/4, lb_subTitle.frame.size.width , lb_subTitle.frame.size.height);
            
        }
            break;
            
        default:
            break;
    }
    
    
    
    if (imgPosition == 3) {
        iv.frame = CGRectMake(ivY, frame.size.height/2, iv.frame.size.width, iv.frame.size.height);
        lb_title.frame = CGRectMake(10,  0, lb_title.frame.size.width, lb_title.frame.size.height);
        lb_subTitle.frame = CGRectMake(10, frame.size.height/4, lb_subTitle.frame.size.width , lb_subTitle.frame.size.height);
        
    }
    
    
    
    
    return btn;
}



//平行的活动按钮
+ (UIButtonEx *)createParallelActivityBtn:(CGRect)frame
                                   object:(NSDictionary *)dictionary
                                      tag:(NSInteger)tag {
    UIButtonEx *btn = [[UIButtonEx alloc] initWithFrame:frame];
    
    
    if ([dictionary objectForKey:@"bgColor"]) {
        [btn setBackgroundColor:[UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"bgColor"]]]];
        
    }
    btn.tag = tag;
    btn.object = dictionary;
    btn.clipsToBounds = YES;
    
    CGColorSpaceRef colorSpaceRef = CGColorSpaceCreateDeviceRGB();
    CGColorRef color = CGColorCreate(colorSpaceRef, (CGFloat[]){0.1,0,0,0.1});
    [btn.layer setBorderWidth:0.5];//设置边界的宽度
    [btn.layer setBorderColor:color];//设置边界的颜色
    
    CGFloat ivY = 10;
    
    UIImageView *iv = [[UIImageView alloc] initWithFrame:CGRectMake(ivY, ivY, APP_VIEW_WIDTH/2-2*ivY, HOME_MODULE_HEIGHT-2*ivY)];
    [btn addSubview:iv];
    iv.backgroundColor = [UIColor whiteColor];
    [btn addSubview:iv];
    if ([dictionary objectForKey:@"imgHeight"]) {
        CGRect ivFrame = iv.frame;
//        CGFloat scale = HOME_MODULE_HEIGHT/[[dictionary objectForKey:@"imgHeight"] floatValue];
        CGFloat imageHeight = [[dictionary objectForKey:@"imgHeight"] floatValue];
        CGFloat imageWidth = [[dictionary objectForKey:@"imgWidth"] floatValue];
        
        ivFrame.size = CGSizeMake(imageWidth, imageHeight);
        iv.frame = ivFrame;
        
    }
    [iv sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dictionary objectForKey:@"imgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    iv.clipsToBounds = YES;
    
    UILabel *lb_title = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT/2)];
    lb_title.textColor = [UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"titleColor"]]];
    lb_title.text = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"title"]];
    lb_title.font = [UIFont systemFontOfSize:TITLE_FONT];
    [btn addSubview:lb_title];
    
    
    UILabel *lb_subTitle = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT/2, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT/2)];
    lb_subTitle.textColor = [UIColor colorWithHexString:[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"subTitleColor"]]];
    lb_subTitle.text = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"subTitle"]];
    lb_subTitle.font = [UIFont systemFontOfSize:SUB_TITLE_FONT];
    [btn addSubview:lb_subTitle];
    
    lb_title.numberOfLines = 0;
    lb_title.lineBreakMode = NSLineBreakByCharWrapping;
    lb_subTitle.numberOfLines = 0;
    lb_subTitle.lineBreakMode = NSLineBreakByCharWrapping;
    
    if ([[dictionary objectForKey:@"imgHeight"] floatValue]>0) {
        iv.frame = CGRectMake(iv.frame.origin.x, iv.frame.origin.y, [[dictionary objectForKey:@"imgWidth"] floatValue], [[dictionary objectForKey:@"imgHeight"] floatValue]);
        lb_title.frame = CGRectMake(iv.frame.origin.y+iv.frame.size.width, 0, APP_VIEW_WIDTH-ivY-iv.frame.size.width, lb_title.frame.size.height);
        lb_subTitle.frame = CGRectMake(iv.frame.origin.y+iv.frame.size.width, lb_subTitle.frame.origin.y, lb_title.frame.size.width, lb_subTitle.frame.size.height);
        
    }
    
    
    int imgPosition = [[dictionary objectForKey:@"imgPosition"] intValue];
    if (imgPosition == 2) {
        iv.frame = CGRectMake(APP_VIEW_WIDTH/2, ivY, iv.frame.size.width, iv.frame.size.height);
        lb_title.frame = CGRectMake(10, 0, APP_VIEW_WIDTH-ivY-iv.frame.size.width, lb_title.frame.size.height);
        lb_subTitle.frame = CGRectMake(10, lb_subTitle.frame.origin.y, lb_title.frame.size.width, lb_subTitle.frame.size.height);
        
    }
    
    return btn;
}


@end
