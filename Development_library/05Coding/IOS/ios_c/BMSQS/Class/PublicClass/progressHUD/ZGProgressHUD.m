//
//  ZGProgressHUD.m
//  NiuNiu
//
//  Created by zltianhen on 11-9-2.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ZGProgressHUD.h"


@implementation ZGProgressHUD
@synthesize hudDelegate;

- (id)initWithFrame:(CGRect)frame hasHUD:(BOOL)ishud
{
    self = [super initWithFrame:frame];
    if (self) 
	{
        if (ishud) 
		{
            m_actView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
            m_actView.frame = CGRectMake(0, 0, m_actView.frame.size.width, m_actView.frame.size.height);
            m_actView.center = CGPointMake(frame.size.width/2, 45);
            [m_actView startAnimating];
            [self addSubview:m_actView];
            
            m_textLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 120, 30)];
            m_textLabel.backgroundColor = [UIColor clearColor];
            m_textLabel.textAlignment = UITextAlignmentCenter;
            m_textLabel.textColor = [UIColor whiteColor];
            m_textLabel.highlighted = YES;
            m_textLabel.center = CGPointMake(frame.size.width/2, 85);
            m_textLabel.font = [UIFont systemFontOfSize:16];
            [self addSubview:m_textLabel];
        } 
		else 
		{
            m_actView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite];
            m_actView.frame = CGRectMake(0, 0, m_actView.frame.size.width, m_actView.frame.size.height);
            m_actView.center = CGPointMake(20, 15);
            [m_actView startAnimating];
            [self addSubview:m_actView];
            
            m_textLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 30)];
            m_textLabel.backgroundColor = [UIColor clearColor];
            m_textLabel.textAlignment = UITextAlignmentCenter;
            m_textLabel.textColor = [UIColor whiteColor];
            m_textLabel.highlighted = YES;
            m_textLabel.center = CGPointMake(frame.size.width/2+16, 15);
            m_textLabel.font = [UIFont systemFontOfSize:16];
            [self addSubview:m_textLabel];
			
			
        }
		
		//self.transform = CGAffineTransformMakeRotation(CC_DEGREES_TO_RADIANS(90.0f));
		self.backgroundColor = [UIColor clearColor];
    }
    return self;
}

- (void)drawRect:(CGRect)rect
{
	CGContextRef context = UIGraphicsGetCurrentContext();
	CGContextSaveGState(context);
	CGContextSetFillColorWithColor(context, [UIColor colorWithRed:0.275 green:0.278 blue:0.286 alpha:0.6].CGColor);
	CGContextBeginPath(context);
	CGContextMoveToPoint(context, rect.size.width/2, 0);
	CGContextAddArcToPoint(context, rect.size.width, 0, rect.size.width, rect.size.height/2, 10);
	CGContextAddArcToPoint(context, rect.size.width, rect.size.height, rect.size.width/2, rect.size.height, 10);
	CGContextAddArcToPoint(context, 0, rect.size.height, 0, rect.size.height/2, 10);
	CGContextAddArcToPoint(context, 0, 0, rect.size.width, 0 ,10);
	CGContextClosePath(context);
	CGContextFillPath(context);
	CGContextRestoreGState(context);
}

- (void)setText:(NSString *)text
{
	m_textLabel.text = text;
}



- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
	if (hudDelegate && [hudDelegate respondsToSelector:@selector(cancalProgressHUD:)])
	{
		[hudDelegate cancalProgressHUD:self];
		//需要在委托后释放
	}
}


@end
