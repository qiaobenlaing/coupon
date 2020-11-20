//
//  BaomiViewUtils.m
//  text
//
//  Created by liuqin on 15/10/31.
//  Copyright © 2015年 singlee.l. All rights reserved.
//

#import "BaomiViewUtils.h"

@implementation BaomiViewUtils




+(UIView *)creatView:(CGRect)frame bgColor:(UIColor *)bgColor cornerRadius:(CGFloat)cornerRadius borderColor:(UIColor *)borderColor  borderWidth:(CGFloat)borderWidth tag:(int)tag{
    UIView *view = [[UIView alloc]initWithFrame:frame];
    view.backgroundColor = bgColor;
    view.layer.borderColor = [borderColor CGColor];
    view.layer.borderWidth = borderWidth;
    view.layer.cornerRadius = 5;
    view.layer.masksToBounds = YES;
    
    return view;
    
    
}



// UIButton  frame title titleColor bgColor
+(UIButton *)creatBtn:(CGRect)frame title:(NSString *)btnTile titleColor:(UIColor *)titleColor bgColor:(UIColor *)bgColor tag:(int)tag{
    
    UIButton *button = [[UIButton alloc]initWithFrame:frame];
    [button setTitle:btnTile forState:UIControlStateNormal];
    [button setTitleColor:titleColor forState:UIControlStateNormal];
    button.backgroundColor = bgColor;
    button.tag = tag;
    
    
    return button;
    
    
}

// UIButton layer
+(UIButton *)creatBtn:(CGRect)frame title:(NSString *)btnTile titleColor:(UIColor *)titleColor bgColor:(UIColor *)bgColor cornerRadius:(CGFloat)cornerRadius borderColor:(UIColor *)borderColor  borderWidth:(CGFloat)borderWidth tag:(int)tag{
    
    UIButton *button = [[UIButton alloc]initWithFrame:frame];
    [button setTitle:btnTile forState:UIControlStateNormal];
    [button setTitleColor:titleColor forState:UIControlStateNormal];
    button.backgroundColor = bgColor;
    button.tag = tag;
    button.layer.cornerRadius = cornerRadius;
    button.layer.masksToBounds = YES;
    button.layer.borderColor =[borderColor CGColor];
    button.layer.borderWidth = borderWidth;
    
    return button;
    
}

+(UIButton *)creatbtn:(CGRect)frame StateNormalImage:(UIImage *)normalImage StateSelectedImage:(UIImage *)seleImage tag:(int)tag{
    
    UIButton *button = [[UIButton alloc]initWithFrame:frame];
    [button setImage:normalImage forState:UIControlStateNormal];
    [button setImage:seleImage forState:UIControlStateSelected];
    button.tag = tag;
    return button;
    
}

//画一条分隔线
+(void)addLine:(UIView *)parent y:(CGFloat)y color:(UIColor *)bgColor{
    
    CGFloat w=parent.frame.size.width-6;
    UILabel *line1View = [[UILabel alloc] initWithFrame:CGRectMake(3, y, w, 1)];
    line1View.backgroundColor = bgColor;
    [parent addSubview:line1View];
}
+(void)addLine:(UIView *)parent y:(CGFloat)y{
    CGFloat w=parent.frame.size.width;
    UILabel *line1View = [[UILabel alloc] initWithFrame:CGRectMake(0, y, w, 1)];
    line1View.backgroundColor = [UIColor grayColor];
    line1View.alpha = 0.613;
    [parent addSubview:line1View];
}

+(UITextField *)creatTextField:(CGRect)frame bgColor:(UIColor *)bgColor  borderColor:(UIColor *)borderColor  borderWidth:(CGFloat)borderWidth tag:(int)tag cornerRadius:(CGFloat)cornerRadius placeholder:(NSString *)placeholderStr font:(UIFont *)font {
    
    UITextField *textField=[[UITextField alloc]initWithFrame:frame];
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = cornerRadius;
    textField.backgroundColor = bgColor;
    textField.layer.borderColor = [borderColor CGColor];
    textField.layer.borderWidth = 0.5;
    textField.placeholder = placeholderStr;
    textField.tag = tag;
    textField.font = font;
    return textField;
}

@end
