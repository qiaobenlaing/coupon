//
//  RemarkImgView.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/11.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "RemarkImgView.h"
#import "UIImageView+WebCache.h"
#import "PhotoScrollView.h"
#import "UITapGestureRecognizerEx.h"
@interface RemarkImgView () <UIScrollViewDelegate>

@property (nonatomic, strong) UIScrollView * scrollView;
@property (nonatomic, strong) NSArray * imageAry;
@property (nonatomic, assign) int   number;
@property (nonatomic, strong) UITapGestureRecognizerEx * tapGesture;
@end

@implementation RemarkImgView



- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        
        [self setRemarkScrollView];
        
    }
    return self;
}

- (void)setRemarkScrollView
{
    self.scrollView = [[UIScrollView alloc] initWithFrame:self.bounds];
    self.scrollView.backgroundColor = [UIColor clearColor];
    [self addSubview:self.scrollView];
    self.scrollView.delegate = self;
    self.scrollView.bounces = YES;
    
}

- (void)setRemarkWithAry:(NSArray *)imageAry
{
    self.imageAry = [NSArray arrayWithArray:imageAry];
    
    for (int i = 1; i <= imageAry.count; i++) {
        
        UIImageView * imageView = [[UIImageView alloc] init];
        
        self.scrollView.contentSize = CGSizeMake(self.scrollView.frame.size.width * (imageAry.count)/2, self.scrollView.frame.size.height);
        imageView.frame = CGRectMake((i - 1) * self.scrollView.frame.size.width/2 + 10, 0, self.scrollView.frame.size.width/2 - 20, self.scrollView.frame.size.height);
        [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,imageAry[i-1][@"remarkImgUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
        imageView.userInteractionEnabled = YES;
        self.tapGesture = [[UITapGestureRecognizerEx alloc]initWithTarget:self action:@selector(showPicture:)];
        self.tapGesture.tapGestureNumber = i-1;
        [imageView addGestureRecognizer:self.tapGesture];
        
        
        [self.scrollView addSubview:imageView];
    }

}


- (void)showPicture:(UITapGestureRecognizerEx *)sender
{
    PhotoScrollView *sc = [[PhotoScrollView alloc]init];
    UIApplication *app = [UIApplication sharedApplication];
    UIWindow *window = app.keyWindow;
    __block PhotoScrollView *_weakPhoto=sc;
    sc.removeSC = ^{
        [_weakPhoto removeFromSuperview];
        
    };
    sc.delegate = self;
    NSArray * array = [NSArray arrayWithArray:self.imageAry];
    sc.count = (int)array.count;
    [sc setParentImageArray:array];
    [sc setImage:sender.tapGestureNumber];
    [window addSubview:sc];
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    for (int i = 1; i <= self.imageAry.count; i++){
        
        if (scrollView.contentSize.width >= ((i - 1) * self.scrollView.frame.size.width/2 + 10)) {
            
            self.number = i-1;
            
        }
        
    }
    
    
}


@end
