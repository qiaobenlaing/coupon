//
//  OrderFinishViewController.m
//  BMSQS
//
//  Created by gh on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OrderFinishViewController.h"
#import "UIImageView+WebCache.h"

@implementation OrderFinishViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
//    [self setNavBackItem];
    [self setRightButton];
    [self setNavTitle:@"支付成功"];
    [self setViewUp];
    
    
}

- (void)setRightButton {
    float statusPosition = APP_STATUSBAR_HEIGHT;
    if (IOS7)
    {
        statusPosition = 0;
    }
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH - 44, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT - statusPosition), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    [button setTitle:@"完成" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.navigationView addSubview:button];
    
    
}
- (void)btnAction:(UIButton *)btn {
    [self.navigationController popToRootViewControllerAnimated:YES];
    
}

- (void)setViewUp {
    
    NSString *avatarUrl = [self.dataDic objectForKey:@"avatarUrl"]; //头像
    NSString *nickName = [self.dataDic objectForKey:@"nickName"]; //用户名
    NSString *orderNbr = [NSString stringWithFormat:@"订单号: %@", [self.dataDic objectForKey:@"orderNbr"]]; //订单号
    NSString *orderAmount = [NSString stringWithFormat:@"%.2f元", [[self.dataDic objectForKey:@"orderAmount"] floatValue]]; //订单金额
    NSString *deduction = [NSString stringWithFormat:@"%.2f元",[[self.dataDic objectForKey:@"deduction"] floatValue]]; //优惠金额
    NSString *realPay = [NSString stringWithFormat:@"%.2f元", [[self.dataDic objectForKey:@"realPay"] floatValue]];// 支付金额
    
    self.view.backgroundColor = UICOLOR(247, 247, 247, 1);
    
    NSString *left = @"left";
    NSString *right = @"right";
    CGFloat viewY = APP_VIEW_ORIGIN_Y;
    CGFloat height = 150;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, viewY, APP_VIEW_WIDTH, height)];
    view.backgroundColor = UICOLOR(255, 255, 255, 1);
    [self.view addSubview:view];
    

    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 40)];
    label.textAlignment = NSTextAlignmentCenter;
    label.text = @"支付成功";
    [view addSubview:label];
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH-50)/2, label.frame.size.height+label.frame.origin.y, 50, 50)];
    imageView.backgroundColor = [UIColor blueColor];
    [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, avatarUrl]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    [view addSubview:imageView];
    
    UILabel *label2 = [[UILabel alloc] initWithFrame:CGRectMake(0, imageView.frame.size.height + imageView.frame.origin.y, APP_VIEW_WIDTH, 30)];
    label2.font = [UIFont systemFontOfSize:13.f];
    label2.textAlignment = NSTextAlignmentCenter;
    label2.text = nickName;
    [view addSubview:label2];
    
    //订单号
    viewY = viewY + height + 10;
    UIView *view1 = [self drawView:viewY];
    [self drawLabel:left Parentview:view1 title:orderNbr];
    
    
    viewY = viewY + 44 + 10;
    UIView *view2 = [self drawView:viewY];
    [self drawLabel:left Parentview:view2 title:@"消费金额"];
    [self drawLabel:right Parentview:view2 title:orderAmount];
    
    viewY = viewY + 44 + 1;
    UIView *view3 = [self drawView:viewY];
    [self drawLabel:left Parentview:view3 title:@"优惠金额"];
    [self drawLabel:right Parentview:view3 title:deduction];
    
    viewY = viewY + 44 + 1;
    UIView *view4 = [self drawView:viewY];
    [self drawLabel:left Parentview:view4 title:@"实际金额"];
    [self drawLabel:right Parentview:view4 title:realPay];
    
    
    
}


- (UILabel *)drawLabel:(NSString *)rect Parentview:(UIView *)view title:(NSString *)titleStr{
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44)];
    label.backgroundColor = [UIColor clearColor];
    label.font = [UIFont systemFontOfSize:13.f];
    label.text = titleStr;
    
    if ([rect isEqualToString:@"left"]) {
        label.textAlignment = NSTextAlignmentLeft;
    }else  {
        label.textAlignment = NSTextAlignmentRight;
    }
    [view addSubview:label];
    return label;
    
}

- (UIView *)drawView:(CGFloat)frameY {
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, frameY, APP_VIEW_WIDTH, 44)];
    view.backgroundColor = UICOLOR(255, 255, 255, 1);
    [self.view addSubview:view];
    return view;
    
}




@end
