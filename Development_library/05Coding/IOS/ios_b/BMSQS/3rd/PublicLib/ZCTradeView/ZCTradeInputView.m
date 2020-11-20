//
//  ZCTradeInputView.m
//  直销银行
//
//  Created by 塔利班 on 15/4/30.
//  Copyright (c) 2015年 联创智融. All rights reserved.
//

#define ZCTradeInputViewNumCount 6

// 快速生成颜色
#define ZCColor(r, g, b) [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:1.0]

typedef enum {
    ZCTradeInputViewButtonTypeWithCancle = 10000,
    ZCTradeInputViewButtonTypeWithOk = 20000,
}ZCTradeInputViewButtonType;

#import "ZCTradeInputView.h"
#import "ZCTradeKeyboard.h"
#import "NSString+Extension.h"

@interface ZCTradeInputView ()


@end

@implementation ZCTradeInputView

#pragma mark - LazyLoad

- (NSMutableArray *)nums
{
    if (_nums == nil) {
        _nums = [NSMutableArray array];
    }
    return _nums;
}

#pragma mark - LifeCircle

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor = [UIColor clearColor];
        /** 注册keyboard通知 */
        [self setupKeyboardNote];
        /** 添加子控件 */
        [self setupSubViews];
    }
    return self;
}

/** 添加子控件 */
- (void)setupSubViews
{
    /** 选择银行卡 */
    UIButton *okBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [self addSubview:okBtn];
    self.okBtn = okBtn;
    
    [self.okBtn addTarget:self action:@selector(btnClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.okBtn setTitle:@"工行卡******1234" forState:UIControlStateNormal];
    [self.okBtn setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
    self.okBtn.titleLabel.font = [UIFont systemFontOfSize:12];
    self.okBtn.backgroundColor = [UIColor clearColor];
    [self.okBtn setImage:[UIImage imageNamed:@"garyright"] forState:UIControlStateNormal];
    self.okBtn.tag = ZCTradeInputViewButtonTypeWithOk;
    [self.okBtn addTarget:self action:@selector(btnClick:) forControlEvents:UIControlEventTouchUpInside];

    
    /** 取消按钮 */
    UIButton *cancleBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [self addSubview:cancleBtn];
    self.cancleBtn = cancleBtn;
    [self.cancleBtn setTitle:@"X" forState:UIControlStateNormal];
    [self.cancleBtn setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    self.cancleBtn.titleLabel.font = [UIFont systemFontOfSize:20];
    [self.cancleBtn addTarget:self action:@selector(btnClick:) forControlEvents:UIControlEventTouchUpInside];
    self.cancleBtn.tag = ZCTradeInputViewButtonTypeWithCancle;
    
    UIView *lineView = [[UIView alloc]init];
    [self addSubview:lineView];
    self.line = lineView;
    self.line.backgroundColor = UICOLOR(220, 219, 222, 1);

    
}

/** 注册keyboard通知 */
- (void)setupKeyboardNote
{
    // 删除通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(delete) name:ZCTradeKeyboardDeleteButtonClick object:nil];
    
    // 确定通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(ok) name:ZCTradeKeyboardOkButtonClick object:nil];
    
    // 数字通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(number:) name:ZCTradeKeyboardNumberButtonClick object:nil];
    
//  清除所有num;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(deleteAllNum) name:@"removeAllNum" object:nil];

}

#pragma mark - Layout

- (void)layoutSubviews
{
    [super layoutSubviews];
    
    /** 取消按钮 */
    self.cancleBtn.width = ZCScreenWidth * 0.128125;
    self.cancleBtn.height = ZCScreenWidth * 0.128125;
    self.cancleBtn.x =self.width-ZCScreenWidth * 0.128125;// ZCScreenWidth * 0.05;
    self.cancleBtn.y = 0;
    
    CGSize size = [@"1" sizeWithFont:[UIFont systemFontOfSize:ZCScreenWidth * 0.053125] andMaxSize:CGSizeMake(MAXFLOAT, MAXFLOAT)];
    CGFloat titleH = size.height;
    
    self.line.y =ZCScreenWidth * 0.03125+ZCScreenWidth * 0.03125*2+titleH*2+5;
    self.line.x =ZCScreenWidth * 0.096875 * 0.5+15;
    self.line.width = self.width-self.line.x*2;
    self.line.height = 0.4;
    
    
    /** 确定按钮 */
    self.okBtn.y = self.line.y+5;
    self.okBtn.width = self.width;
    self.okBtn.height =30;
    self.okBtn.x =0;
    [self.okBtn setTitle:self.cardMessagte forState:UIControlStateNormal];
    self.okBtn.imageEdgeInsets = UIEdgeInsetsMake(0,self.width-50, 0, 0);
    self.okBtn.titleLabel.textAlignment = NSTextAlignmentLeft;
    
 
    
}

#pragma mark - Private

// 删除
- (void)delete
{
    [self.nums removeLastObject];
    [self setNeedsDisplay];
}

-(void)deleteAllNum{
    [self.nums removeAllObjects];
    [self setNeedsDisplay];
}

// 数字
- (void)number:(NSNotification *)note
{
    if (self.nums.count >= ZCTradeInputViewNumCount) return;
    NSDictionary *userInfo = note.userInfo;
    NSNumber *numObj = userInfo[ZCTradeKeyboardNumberKey];
    [self.nums addObject:numObj];
    [self setNeedsDisplay];
}

// 确定
- (void)ok
{
    
}

// 按钮点击
- (void)btnClick:(UIButton *)btn
{
    if (btn.tag == ZCTradeInputViewButtonTypeWithCancle) {  // 取消按钮点击
        if ([self.delegate respondsToSelector:@selector(tradeInputView:cancleBtnClick:)]) {
            [self.delegate tradeInputView:self cancleBtnClick:btn];
        }
        [[NSNotificationCenter defaultCenter] postNotificationName:ZCTradeInputViewCancleButtonClick object:self];
    } else if (btn.tag == ZCTradeInputViewButtonTypeWithOk) {  // 确定按钮点击
        if ([self.delegate respondsToSelector:@selector(tradeInputView:okBtnClick:)]) {
            [self.delegate tradeInputView:self okBtnClick:btn];
        }
//        // 包装通知字典
//        NSMutableString *pwd = [NSMutableString string];
//        for (int i = 0; i < self.nums.count; i++) {
//            NSString *str = [NSString stringWithFormat:@"%@", self.nums[i]];
//            [pwd appendString:str];
//        }
//        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
//        dict[ZCTradeInputViewPwdKey] = pwd;
//        [[NSNotificationCenter defaultCenter] postNotificationName:ZCTradeInputViewOkButtonClick object:self userInfo:dict];
        
        [[NSNotificationCenter defaultCenter] postNotificationName:@"seleCard_val" object:self userInfo:nil];

        
    } else {
        
    }
}

- (void)drawRect:(CGRect)rect
{
    // 画图
    UIImage *bg = [UIImage imageNamed:@"trade.bundle/pssword_bg"];
    [bg drawInRect:rect];
 
    
    // 画字
    NSString *title = self.title.length>0?self.title:@"请输入支付密码";
//
    CGSize size = [title sizeWithFont:[UIFont systemFontOfSize:ZCScreenWidth * 0.053125] andMaxSize:CGSizeMake(MAXFLOAT, MAXFLOAT)];
    CGFloat titleW = size.width;
    CGFloat titleH = size.height;
    CGFloat titleX = (self.width - titleW) * 0.5;
    CGFloat titleY = ZCScreenWidth * 0.03125;
    CGRect titleRect = CGRectMake(titleX, titleY, titleW, titleH);
    
    NSMutableDictionary *attr = [NSMutableDictionary dictionary];
    attr[NSFontAttributeName] = [UIFont systemFontOfSize:ZCScreenWidth * 0.053125];
    attr[NSForegroundColorAttributeName] = ZCColor(102, 102, 102);
    
    [title drawInRect:titleRect withAttributes:attr];
    
    if (self.isModify) {
        
        UIImage *field = [UIImage imageNamed:@"trade.bundle/password_in"];
        CGFloat x = (self.width-(ZCScreenWidth * 0.846875))/2;//ZCScreenWidth * 0.096875 * 0.5;
        CGFloat y = ZCScreenWidth * 0.40625 * 0.5;
        CGFloat w = ZCScreenWidth * 0.846875;
        CGFloat h = ZCScreenWidth * 0.121875;
        
        [field drawInRect:CGRectMake(x, y, w, h)];
        
        // 画点
        UIImage *pointImage = [UIImage imageNamed:@"trade.bundle/yuan"];
        CGFloat pointW = ZCScreenWidth * 0.05;
        CGFloat pointH = pointW;
        CGFloat pointY = ZCScreenWidth * 0.24;
        CGFloat pointX;
        CGFloat margin = ZCScreenWidth * 0.0484375+10;
        CGFloat padding = ZCScreenWidth * 0.045578125;
        for (int i = 0; i < self.nums.count; i++) {
            pointX = margin + padding + i * (pointW + 2 * padding);
            [pointImage drawInRect:CGRectMake(pointX, pointY, pointW, pointH)];
        }
        
        
        if (self.nums.count == ZCTradeInputViewNumCount) {
            
            NSArray *array = [NSArray arrayWithObjects:self.nums,self, nil];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"overNum" object:array];
            
        }
        
    }else{
        NSString *userMobile = @"申请人手机号";
        size = [title sizeWithFont:[UIFont systemFontOfSize:13.f] andMaxSize:CGSizeMake(MAXFLOAT, MAXFLOAT)];
        titleY = 50;
        titleW = size.width;
        titleH = size.height;
        titleX = 10;
        titleRect = CGRectMake(titleX, titleY, titleW, titleH);
        
        NSMutableDictionary *attr_userMobile = [NSMutableDictionary dictionary];
        attr_userMobile[NSFontAttributeName] = [UIFont systemFontOfSize:13.f];
        attr_userMobile[NSForegroundColorAttributeName] = UICOLOR(155, 155, 155, 1);// ZCColor(102, 102, 102);
        [userMobile drawInRect:titleRect withAttributes:attr_userMobile];
        
        
        NSString *userMobileNum = self.mobileNum;
        size = [title sizeWithFont:[UIFont systemFontOfSize:13.f] andMaxSize:CGSizeMake(MAXFLOAT, MAXFLOAT)];
        titleY = 50;
        titleW = size.width;
        titleH = size.height;
        titleX = self.width-size.width;
        titleRect = CGRectMake(titleX, titleY, titleW, titleH);
        
        NSMutableDictionary *attr_Mobile = [NSMutableDictionary dictionary];
        attr_Mobile[NSFontAttributeName] = [UIFont systemFontOfSize:13.f];
        attr_Mobile[NSForegroundColorAttributeName] = UICOLOR(155, 155, 155, 1);// ZCColor(102, 102, 102);
        [userMobileNum drawInRect:titleRect withAttributes:attr_Mobile];
        
        
        
        //    // 画字
        NSString *price = self.priceStr;
        size = [title sizeWithFont:[UIFont systemFontOfSize:15.f] andMaxSize:CGSizeMake(MAXFLOAT, MAXFLOAT)];
        titleY = titleY+titleH;
        titleW = size.width;
        titleH = size.height;
        titleX = (self.width - titleW) * 0.5;
        titleRect = CGRectMake(titleX, titleY, titleW, titleH);
        
        NSMutableDictionary *attr_price = [NSMutableDictionary dictionary];
        attr_price[NSFontAttributeName] = [UIFont systemFontOfSize:15.f];
        attr_price[NSForegroundColorAttributeName] = APP_NAVCOLOR;// ZCColor(102, 102, 102);
        [price drawInRect:titleRect withAttributes:attr_price];
        
        
        
        UIImage *field = [UIImage imageNamed:@"trade.bundle/password_in"];
        CGFloat x = ZCScreenWidth * 0.096875 * 0.5;
        CGFloat y = titleY+10 +  50;
        CGFloat w = ZCScreenWidth * 0.846875;
        CGFloat h = ZCScreenWidth * 0.121875;
        
        [field drawInRect:CGRectMake(x, y, w, h)];
        
        // 画点
        UIImage *pointImage = [UIImage imageNamed:@"trade.bundle/yuan"];
        CGFloat pointW = ZCScreenWidth * 0.05;
        CGFloat pointH = pointW;
        CGFloat pointY = y+10;
        CGFloat pointX;
        CGFloat margin = ZCScreenWidth * 0.0484375;
        CGFloat padding = ZCScreenWidth * 0.045578125;
        for (int i = 0; i < self.nums.count; i++) {
            pointX = margin + padding + i * (pointW + 2 * padding);
            [pointImage drawInRect:CGRectMake(pointX, pointY, pointW, pointH)];
        }
        
        
        if (self.nums.count == ZCTradeInputViewNumCount) {
            
            [[NSNotificationCenter defaultCenter]postNotificationName:@"overNum" object:self.nums];
            
        }
    }
  
    
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end
// 版权属于原作者
// http://code4app.com (cn) http://code4app.net (en)
// 发布代码于最专业的源码分享网站: Code4App.com