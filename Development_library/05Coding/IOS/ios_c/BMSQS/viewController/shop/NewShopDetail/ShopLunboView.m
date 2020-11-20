//
//  ShopLunboView.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ShopLunboView.h"

@interface ShopLunboView ()<UIScrollViewDelegate>

@property (nonatomic, strong)UIScrollView *sc;
@property (nonatomic, strong)UIPageControl *pageControl;
@property (nonatomic, strong)UILabel * numberLabel;
@property (nonatomic, assign) int       decImgCount;
@end

@implementation ShopLunboView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.sc = [[UIScrollView alloc]initWithFrame:CGRectZero];
        self.sc.pagingEnabled = YES;
        self.sc.delegate = self;
        [self addSubview:self.sc];
        
        self.pageControl = [[UIPageControl alloc]initWithFrame:CGRectMake(0, 0, 100, 30)];
        [self addSubview:self.pageControl];
        [self bringSubviewToFront:self.pageControl];
        
        
        self.numberLabel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 40, self.frame.size.height - 20, 37, 17)];
        _numberLabel.backgroundColor = [UIColor blackColor];
//        _numberLabel.text = [NSString stringWithFormat:@"%li", (long)num];
        _numberLabel.textColor = [UIColor redColor];
        _numberLabel.textAlignment = NSTextAlignmentCenter;
        _numberLabel.font = [UIFont systemFontOfSize:9];
        [self addSubview:self.numberLabel];
        
    }
    return self;
}

-(void)setHomeAdCell:(NSArray *)subList height:(float)height{
    self.sc.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, height) ;
    
    if (subList.count>1) {
        self.pageControl.hidden = NO;
        self.pageControl.center = CGPointMake(APP_VIEW_WIDTH/2, height-10);
        self.pageControl.numberOfPages = subList.count;
        self.pageControl.currentPage = 0;
        
    }else{
        self.pageControl.hidden = YES;
    }
    
    
    for (UIView *aboveV in self.sc.subviews) {
        [aboveV removeFromSuperview];
    }
    
    self.decImgCount = (int)subList.count;
    
    for (int i=0; i<subList.count; i++) {
        NSDictionary *subDic = [subList objectAtIndex:i];
        UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(i*APP_VIEW_WIDTH, 0, APP_VIEW_WIDTH, self.sc.frame.size.height)];
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[subDic objectForKey:@"imgUrl"]]];
        [imageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        [self.sc addSubview:imageView];
        _numberLabel.text = [NSString stringWithFormat:@"%d/%li", 1, (unsigned long)subList.count];
        imageView.tag = 100+i;
        self.sc.userInteractionEnabled = YES;
        imageView.userInteractionEnabled = YES;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(TapGesture:)];
        [imageView addGestureRecognizer:tap];
    }
    self.sc.contentSize = CGSizeMake(APP_VIEW_WIDTH*subList.count, self.sc.frame.size.height);
    
}
-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    CGFloat pageWidth = scrollView.frame.size.width;
    int page = floor((scrollView.contentOffset.x - pageWidth / 2) / pageWidth) + 1;
    self.pageControl.currentPage = page;
    
    _numberLabel.text = [NSString stringWithFormat:@"%d/%d", page + 1, self.decImgCount];
    
}


-(void)TapGesture:(UITapGestureRecognizer *)tap{
    [self.adDelegate clickAD:(int)tap.view.tag-100];
}



@end
